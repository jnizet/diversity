package fr.mnhn.diversity.indicator;

import java.util.List;
import java.util.stream.Collectors;

import fr.mnhn.diversity.indicator.api.ValuedIndicator;

/**
 * The indicator values fetched from the API, with the short label of the indicator
 * @author JB Nizet
 */
public final class ValuedIndicatorDTO {
    private final String shortLabel;
    private final List<IndicatorValueDTO> values;

    public ValuedIndicatorDTO(String shortLabel, List<IndicatorValueDTO> values) {
        this.shortLabel = shortLabel;
        this.values = values;
    }

    public ValuedIndicatorDTO(ValuedIndicator indicator) {
        this(indicator.getData().getShortLabel(),
             indicator.getValues()
                      .entrySet()
                      .stream()
                      .map(entry -> new IndicatorValueDTO(entry.getKey(), entry.getValue()))
                      .collect(Collectors.toUnmodifiableList()));
    }

    public String getShortLabel() {
        return shortLabel;
    }

    public List<IndicatorValueDTO> getValues() {
        return values;
    }
}
