package fr.mnhn.diversity.admin.security;

import java.util.Objects;

/**
 * An authenticated user
 * @author JB Nizet
 */
public final class AuthenticatedUserDTO {
    private final Long id;
    private final String login;
    private final String token;

    public AuthenticatedUserDTO(Long id, String login, String token) {
        this.id = id;
        this.login = login;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthenticatedUserDTO)) {
            return false;
        }
        AuthenticatedUserDTO that = (AuthenticatedUserDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(login, that.login) &&
            Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, token);
    }

    @Override
    public String toString() {
        return "AuthenticatedUserDTO{" +
            "id=" + id +
            ", login='" + login + '\'' +
            ", token='" + token + '\'' +
            '}';
    }
}
