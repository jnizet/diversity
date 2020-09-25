package fr.mnhn.diversity.admin.security;

import java.util.Objects;

/**
 * A User (admin), as stored in the database
 * @author JB Nizet
 */
public final class User {
    private final Long id;
    private final String login;
    private final String hashedPassword;

    public User(Long id, String login, String hashedPassword) {
        this.id = id;
        this.login = login;
        this.hashedPassword = hashedPassword;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) &&
            Objects.equals(login, user.login) &&
            Objects.equals(hashedPassword, user.hashedPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, hashedPassword);
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", login='" + login + '\'' +
            ", hashedPassword='" + hashedPassword + '\'' +
            '}';
    }
}
