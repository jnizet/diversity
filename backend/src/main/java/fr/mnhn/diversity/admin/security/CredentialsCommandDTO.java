package fr.mnhn.diversity.admin.security;

import java.util.Objects;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Credentials sent to authenticate
 * @author JB Nizet
 */
public final class CredentialsCommandDTO {
    @NotBlank
    private final String login;
    @NotBlank
    private final String password;

    @JsonCreator
    public CredentialsCommandDTO(@JsonProperty("login") String login,
                                 @JsonProperty("password") String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CredentialsCommandDTO)) {
            return false;
        }
        CredentialsCommandDTO that = (CredentialsCommandDTO) o;
        return Objects.equals(login, that.login) &&
            Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    @Override
    public String toString() {
        return "CredentialsDTO{" +
            "login='" + login + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
