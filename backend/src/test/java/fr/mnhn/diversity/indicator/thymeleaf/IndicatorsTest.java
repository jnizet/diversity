package fr.mnhn.diversity.indicator.thymeleaf;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import fr.mnhn.diversity.indicator.IndicatorValue;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link Indicators}
 * @author JB Nizet
 */
class IndicatorsTest {
    @Test
    void shouldFormatValue() {
        Indicators indicators = new Indicators(Locale.FRANCE);
        assertThat(indicators.formatValue(new IndicatorValue(123.4567, null))).isEqualTo("123,46");
        assertThat(indicators.formatValue(new IndicatorValue(123.4, null))).isEqualTo("123,4");
        assertThat(indicators.formatValue(new IndicatorValue(123.0, null))).isEqualTo("123");
        assertThat(indicators.formatValue(new IndicatorValue(123.45, "%"))).isEqualTo("123,45\u00a0%");
    }
}
