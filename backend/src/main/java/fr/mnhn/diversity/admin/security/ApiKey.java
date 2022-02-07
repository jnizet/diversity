package fr.mnhn.diversity.admin.security;

import java.util.Objects;

public class ApiKey {

    private final String host;
    private final String key;

    public ApiKey(String host, String key) {
        this.host = host;
        this.key = key;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApiKey)) {
            return false;
        }
        ApiKey apiKey = (ApiKey) o;
        return Objects.equals(host, apiKey.host) &&
            Objects.equals(key, apiKey.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, key);
    }

    @Override
    public String toString() {
        return "User{" +
            "host=" + host +
            ", key='" + key + '\'' +
            '}';
    }

    public String getHost() {
        return host;
    }

    public String getKey() {
        return key;
    }
}
