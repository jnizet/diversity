package fr.mnhn.diversity.matomo;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("diversity.matomo")
class MatomoProperties {
    private String host;
    private String siteId;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
