package fr.mnhn.diversity.indicator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import fr.mnhn.diversity.territory.Territory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * The repository used to handle indicators and their values
 */
@Repository
public class IndicatorRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public IndicatorRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Lists all the indicators and their categories
     */
    public List<Indicator> list() {
        String query = "select indicator.id, indicator.biom_id, cat.id as category_id, cat.name as category_name from indicator" +
                " left outer join indicator_category ind_cat on indicator.id = ind_cat.indicator_id" +
                " left outer join category cat on cat.id = ind_cat.category_id";
        Map<Long, Indicator> indicatorsById = new HashMap<>();
        Map<Long, List<IndicatorCategory>> categoriesByIndicatorId = new HashMap<>();
        jdbcTemplate.query(query, (rs) -> {
            long indicatorId = rs.getLong("id");
            String biomId = rs.getString("biom_id");
            indicatorsById.computeIfAbsent(indicatorId, id -> new Indicator(indicatorId, biomId));
            List<IndicatorCategory> categoriesForIndicator = categoriesByIndicatorId.computeIfAbsent(indicatorId, id -> new ArrayList<>());
            long categoryId = rs.getLong("category_id");
            if (!rs.wasNull()) { // if an indicator has a category
                String categoryName = rs.getString("category_name");
                categoriesForIndicator.add(new IndicatorCategory(categoryId, categoryName));
            }
        });
        return indicatorsById.values()
                             .stream()
                             .sorted(Comparator.comparing(Indicator::getBiomId))
                             .map(indicator -> new Indicator(indicator.getId(),
                                                             indicator.getBiomId(),
                                                             categoriesByIndicatorId.get(indicator.getId())
                                                                                    .stream()
                                                                                    .sorted(Comparator.comparing(IndicatorCategory::getName))
                                                                                    .collect(Collectors.toList())
                             ))
                             .collect(Collectors.toList());
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
    public IndicatorValues getValues(Indicator indicator) {
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
                    return new IndicatorValues(values);
                });
    }
}
