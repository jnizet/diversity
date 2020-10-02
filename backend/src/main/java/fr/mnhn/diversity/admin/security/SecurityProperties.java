package fr.mnhn.diversity.admin.security;

import java.util.Objects;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Security-related properties
 * @author JB Nizet
 */
@ConfigurationProperties("diversity.security")
@Validated
public class SecurityProperties {

    @NotBlank
    private String secretKey;

    public SecurityProperties(String secretKey) {
        this.secretKey = secretKey;
    }

    public SecurityProperties() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SecurityProperties)) {
            return false;
        }
        SecurityProperties that = (SecurityProperties) o;
        return Objects.equals(secretKey, that.secretKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(secretKey);
    }

    @Override
    public String toString() {
        return "SecurityProperties{" +
            "secretKey='" + secretKey + '\'' +
            '}';
    }
}
