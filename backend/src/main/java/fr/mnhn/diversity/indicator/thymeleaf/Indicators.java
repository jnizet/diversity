package fr.mnhn.diversity.indicator.thymeleaf;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import fr.mnhn.diversity.indicator.IndicatorValue;
import org.springframework.util.StringUtils;

/**
 * A utility class exposed as an expression object to thymeleaf templates by {@link IndicatorDialect}
 * @author JB Nizet
 */
public class Indicators {
    private final Locale locale;

    public Indicators(Locale locale) {
        this.locale = locale;
    }

    /**
     * Formats an indicator value as a number with 0 to 2 decimal digits and, if any, the unit of the value
     */
    public String formatValue(IndicatorValue indicatorValue) {
        String formattedNumber = formatValueWithoutUnit(indicatorValue);
        if (StringUtils.hasText(indicatorValue.getUnit())) {
            return formattedNumber + "\u00a0" + indicatorValue.getUnit();
        }
        else {
            return formattedNumber;
        }
    }

    /**
     * Formats an indicator value as a number with 0 to 2 decimal digits and, if any, the unit of the value
     */
    public String formatValueWithoutUnit(IndicatorValue indicatorValue) {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        decimalFormat.setMinimumIntegerDigits(1);
        decimalFormat.setMinimumFractionDigits(0);
        decimalFormat.setMaximumFractionDigits(2);
        return decimalFormat.format(indicatorValue.getValue());
    }
}
