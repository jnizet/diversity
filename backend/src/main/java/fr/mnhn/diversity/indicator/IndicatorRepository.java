package fr.mnhn.diversity.indicator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import fr.mnhn.diversity.ecogesture.Ecogesture;
import fr.mnhn.diversity.territory.Territory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * The repository used to handle indicators and their values
 */
@Repository
public class IndicatorRepository {

    private static final String INDICATOR_QUERY = "select indicator.id, indicator.biom_id, indicator.slug, indicator.is_rounded," +
        " cat.id as category_id, cat.name as category_name," +
        " eco.id as ecogesture_id, eco.slug as ecogesture_slug from indicator" +
        " left outer join indicator_ecogesture ind_eco on indicator.id = ind_eco.indicator_id" +
        " left outer join ecogesture eco on eco.id = ind_eco.ecogesture_id" +
        " left outer join indicator_category ind_cat on indicator.id = ind_cat.indicator_id" +
        " left outer join category cat on cat.id = ind_cat.category_id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public IndicatorRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Lists all the indicators and their categories
     */
    public List<Indicator> list() {
        return jdbcTemplate.query(INDICATOR_QUERY, this::extractIndicators);
    }

    /**
     * Deletes the indicator values for the given indicator and territories
     */
    public void deleteValues(Indicator indicator, Set<Territory> territories) {
        if (territories.isEmpty()) {
            return;
        }
        Map<String, Object> paramMap = Map.of(
            "indicator_id", indicator.getId(),
            "territories", territories.stream().map(Territory::name).collect(Collectors.toSet())
        );
        jdbcTemplate.update("delete from indicator_value" +
                                " where indicator_id = :indicator_id and territory in (:territories)",
                            paramMap);
    }

    /**
     * Creates a new {@link Indicator}
     */
    public Indicator create(Indicator indicator) {
        // create the indicator
        Map<String, Object> paramMap = Map.of(
            "biomId", indicator.getBiomId(),
            "slug", indicator.getSlug(),
            "isRounded", indicator.getIsRounded()
        );
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update("insert into indicator (id, biom_id, slug, is_rounded) values (nextval('indicator_seq'), :biomId, :slug, :isRounded)", new MapSqlParameterSource(paramMap), keyHolder);
        Long id = (Long) keyHolder.getKeys().get("id");

        // add the categories
        indicator.getCategories().forEach(category -> {
            Map<String, Object> categoryParamMap = Map.of(
                "id", id,
                "category_id", category.getId()
            );
            jdbcTemplate.update("insert into indicator_category (indicator_id, category_id) values (:id, :category_id)", categoryParamMap);
        });

        // add the ecogestures
        indicator.getEcogestures().forEach(ecogesture -> {
            Map<String, Object> ecogestureParamMap = Map.of(
                "id", id,
                "ecogesture_id", ecogesture.getId()
            );
            jdbcTemplate.update("insert into indicator_ecogesture (indicator_id, ecogesture_id) values (:id, :ecogesture_id)", ecogestureParamMap);
        });

        return new Indicator(id, indicator.getBiomId(), indicator.getSlug(), indicator.getIsRounded(), indicator.getCategories(), indicator.getEcogestures());
    }

    /**
     * Updates an {@link Indicator}
     */
    public Indicator update(Indicator indicator) {
        // update the indicator
        Map<String, Object> paramMap = Map.of(
            "id", indicator.getId(),
            "biomId", indicator.getBiomId(),
            "slug", indicator.getSlug(),
            "isRounded", indicator.getIsRounded()
        );
        jdbcTemplate.update("update indicator set biom_id = :biomId, slug = :slug, is_rounded = :isRounded where id = :id", paramMap);

        // update the categories
        removeCategoriesFromIndicator(indicator.getId());
        indicator.getCategories().forEach(category -> {
            Map<String, Object> categoryParamMap = Map.of(
                "id", indicator.getId(),
                "category_id", category.getId()
            );
            jdbcTemplate.update("insert into indicator_category (indicator_id, category_id) values (:id, :category_id)", categoryParamMap);
        });

        // update the ecogestures
        removeEcogesturesFromIndicator(indicator.getId());
        indicator.getEcogestures().forEach(ecogesture -> {
            Map<String, Object> ecogestureParamMap = Map.of(
                "id", indicator.getId(),
                "ecogesture_id", ecogesture.getId()
            );
            jdbcTemplate.update("insert into indicator_ecogesture (indicator_id, ecogesture_id) values (:id, :ecogesture_id)", ecogestureParamMap);
        });

        return new Indicator(indicator.getId(), indicator.getBiomId(), indicator.getSlug(),
            indicator.getIsRounded(), indicator.getCategories(), indicator.getEcogestures());
    }

    /**
     * Deletes the indicator and its values
     */
    public void delete(Indicator indicator) {
        // delete values
        deleteValues(indicator, Set.of(Territory.values()));
        // delete from categories join table
        removeCategoriesFromIndicator(indicator.getId());
        // delete from ecogestures join table
        removeEcogesturesFromIndicator(indicator.getId());
        // delete from eco-gestures join table
        Map<String, Object> paramMap = Map.of(
            "id", indicator.getId()
        );
        // delete the indicator
        jdbcTemplate.update("delete from indicator where id = :id", paramMap);
    }

    private void removeCategoriesFromIndicator(Long indicatorId) {
        Map<String, Object> paramMap = Map.of(
            "id", indicatorId
        );
        jdbcTemplate.update("delete from indicator_category where indicator_id = :id", paramMap);
    }

    private void removeEcogesturesFromIndicator(Long indicatorId) {
        Map<String, Object> paramMap = Map.of(
            "id", indicatorId
        );
        jdbcTemplate.update("delete from indicator_ecogesture where indicator_id = :id", paramMap);
    }

    public List<Indicator> findIndicatorsForEcogesture(String ecogestureSlug) {
        String query = INDICATOR_QUERY + " where eco.slug = :slug";
        return jdbcTemplate.query(query, Map.of("slug", ecogestureSlug), this::extractIndicators);
    }

    /**
     * Updates the indicator value for the given indicator and territory
     * @return true if the value was updated, false if not, because the value didn't exist yet.
     */
    public boolean updateValue(Indicator indicator, Territory territory, IndicatorValue indicatorValue) {
        Map<String, Object> paramMap = Map.of(
            "indicator_id", indicator.getId(),
            "territory", territory.name(),
            "value", indicatorValue.getValue(),
            "unit", indicatorValue.getUnit()
        );
        int updatedRows =
            jdbcTemplate.update("update indicator_value set value = :value, unit = :unit" +
                                    " where indicator_id = :indicator_id and territory = :territory",
                                paramMap);
        return updatedRows > 0;
    }

    /**
     * Inserts a new IndicatorValue for the indicator and terrritory
     */
    public void insertValue(Indicator indicator, Territory territory, IndicatorValue indicatorValue) {
        Map<String, Object> paramMap = Map.of(
            "indicator_id", indicator.getId(),
            "territory", territory.name(),
            "value", indicatorValue.getValue(),
            "unit", indicatorValue.getUnit()
        );
        jdbcTemplate.update("insert into indicator_value (id, indicator_id, territory, value, unit) values (nextval('indicator_value_seq'), :indicator_id, :territory, :value, :unit)", paramMap);
    }

    /**
     * Gets all the values per territory for an indicator
     */
    public Map<Territory, IndicatorValue> getValues(Indicator indicator) {
        return jdbcTemplate.query(
            "select value, unit, territory from indicator_value where indicator_id = :indicator_id",
            Map.of("indicator_id", indicator.getId()),
            (rs) -> {
                Map<Territory, IndicatorValue> values = new HashMap<>();
                while (rs.next()) {
                    String territoryKey = rs.getString("territory");
                    IndicatorValue value = new IndicatorValue(rs.getDouble("value"), rs.getString("unit"));
                    values.put(Territory.valueOf(territoryKey), value);
                }
                return values;
            });
    }

    /**
     * Gets all the values per territory for an indicator
     */
    public Map<Indicator, IndicatorValue> getValuesForIndicatorsAndTerritory(Collection<Indicator> indicators, Territory territory) {
        if (indicators.isEmpty()) {
            return Map.of();
        }

        Map<Long, Indicator> indicatorsById =
            indicators.stream().collect(Collectors.toMap(Indicator::getId, Function.identity()));

        return jdbcTemplate.query(
            "select indicator_id, value, unit from indicator_value" +
                " where indicator_id in (:indicatorIds) and territory = :territory",
            Map.of("indicatorIds", indicatorsById.keySet(),
                   "territory", territory.name()),
            (rs) -> {
                Map<Indicator, IndicatorValue> result = new HashMap<>();
                while (rs.next()) {
                    Long indicatorId = rs.getLong("indicator_id");
                    IndicatorValue value = new IndicatorValue(rs.getDouble("value"), rs.getString("unit"));
                    result.put(indicatorsById.get(indicatorId), value);
                }
                return result;
            });
    }

    /**
     * Gets the values for a given territory and a set of indicator slugs
     */
    public Map<String, IndicatorValue> getValuesForIndicatorSlugsAndTerritory(Collection<String> indicatorSlugs, Territory territory) {
        if (indicatorSlugs.isEmpty()) {
            return Map.of();
        }

        return jdbcTemplate.query(
            "select indicator.slug, iv.value, iv.unit from indicator_value iv" +
                " inner join indicator on indicator.id = iv.indicator_id" +
                " where indicator.slug in (:indicatorSlugs) and territory = :territory",
            Map.of("indicatorSlugs", indicatorSlugs,
                   "territory", territory.name()),
            (rs) -> {
                Map<String, IndicatorValue> result = new HashMap<>();
                while (rs.next()) {
                    String indicatorSlug = rs.getString("slug");
                    IndicatorValue value = new IndicatorValue(rs.getDouble("value"), rs.getString("unit"));
                    result.put(indicatorSlug, value);
                }
                return result;
            });
    }

    public Optional<IndicatorValue> getValueForIndicatorAndTerritory(Indicator indicator, Territory territory) {
        return Optional.ofNullable(
            getValuesForIndicatorsAndTerritory(Set.of(indicator), territory).get(indicator)
        );
    }

    public Optional<Indicator> findById(Long id) {
        String query = INDICATOR_QUERY + " where indicator.id = :id";
        List<Indicator> indicators = jdbcTemplate.query(query, Map.of("id", id), this::extractIndicators);
        return indicators.isEmpty() ? Optional.empty() : Optional.of(indicators.get(0));
    }

    public Optional<Indicator> findBySlug(String slug) {
        String query = INDICATOR_QUERY + " where indicator.slug = :slug";
        List<Indicator> indicators = jdbcTemplate.query(query, Map.of("slug", slug), this::extractIndicators);
        return indicators.isEmpty() ? Optional.empty() : Optional.of(indicators.get(0));
    }

    public Optional<Indicator> findByBiomId(String biomId) {
        String query = INDICATOR_QUERY + " where indicator.biom_id = :biomId";
        List<Indicator> indicators = jdbcTemplate.query(query, Map.of("biomId", biomId), this::extractIndicators);
        return indicators.isEmpty() ? Optional.empty() : Optional.of(indicators.get(0));
    }

    private List<Indicator> extractIndicators(ResultSet rs) throws SQLException {
        Map<Long, Indicator> indicatorsById = new HashMap<>();
        Map<Long, HashSet<IndicatorCategory>> categoriesByIndicatorId = new HashMap<>();
        Map<Long, HashSet<Ecogesture>> ecogesturesByIndicatorId = new HashMap<>();
        while (rs.next()) {
            long indicatorId = rs.getLong("id");
            String biomId = rs.getString("biom_id");
            String slug = rs.getString("slug");
            Boolean isRounded = rs.getBoolean("is_rounded");
            indicatorsById.computeIfAbsent(indicatorId, id -> new Indicator(indicatorId, biomId, slug, isRounded));
            HashSet<IndicatorCategory> categoriesForIndicator = categoriesByIndicatorId.computeIfAbsent(indicatorId,
                                                                                                     id -> new HashSet<>());
            long categoryId = rs.getLong("category_id");
            if (!rs.wasNull()) { // if an indicator has a category
                String categoryName = rs.getString("category_name");
                categoriesForIndicator.add(new IndicatorCategory(categoryId, categoryName));
            }
            HashSet<Ecogesture> ecogesturesForIndicator = ecogesturesByIndicatorId.computeIfAbsent(indicatorId,
                                                                                                id -> new HashSet<>());
            long ecogestureId = rs.getLong("ecogesture_id");
            if (!rs.wasNull()) { // if an indicator has an ecogesture
                String ecogestureSlug = rs.getString("ecogesture_slug");
                ecogesturesForIndicator.add(new Ecogesture(ecogestureId, ecogestureSlug));
            }
        }

        return indicatorsById.values()
                             .stream()
                             .sorted(Comparator.comparing(Indicator::getBiomId))
                             .map(indicator -> new Indicator(indicator.getId(),
                                                             indicator.getBiomId(),
                                                             indicator.getSlug(),
                                                             indicator.getIsRounded(),
                                                             categoriesByIndicatorId.get(indicator.getId())
                                                                                    .stream()
                                                                                    .sorted(Comparator.comparing(IndicatorCategory::getName))
                                                                                    .collect(Collectors.toList()),
                                                             ecogesturesByIndicatorId.get(indicator.getId())
                                                                                     .stream()
                                                                                     .sorted(Comparator.comparing(Ecogesture::getSlug))
                                                                                     .collect(Collectors.toList())
                             ))
                             .collect(Collectors.toList());
    }
}
