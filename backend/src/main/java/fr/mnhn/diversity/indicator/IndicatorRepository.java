package fr.mnhn.diversity.indicator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Indicator> list() {
        return jdbcTemplate.query("select id, biom_id from indicator order by biom_id", (rs, rowNum) -> new Indicator(rs.getLong("id"), rs.getString("biom_id")));
    }

    /**
     * Saves a new IndicatorValue for the indicator and terrritory
     */
    public void saveValue(Indicator indicator, Territory territory, IndicatorValue indicatorValue) {
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
