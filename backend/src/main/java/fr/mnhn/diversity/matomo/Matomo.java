package fr.mnhn.diversity.matomo;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

public final class Matomo {

    private final MatomoProperties matomoProperties;

    public Matomo(MatomoProperties matomoProperties) {
        this.matomoProperties = matomoProperties;
    }

    public String getHost() {
        return matomoProperties.getHost();
    }

    public String getSiteId() {
        return matomoProperties.getSiteId();
    }

    public boolean isEnabled() {
        return StringUtils.hasText(getHost()) && StringUtils.hasText(getSiteId());
    }
}
