package fr.mnhn.diversity.admin.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link PasswordHasher}
 * @author JB Nizet
 */
class PasswordHasherTest {

    private PasswordHasher hasher = new PasswordHasher();

    @Test
    void shouldMatchHashedPassword() {
        String password = "password";
        String hash = hasher.hash(password);
        assertThat(hasher.match(password, hash)).isTrue();
    }

    @Test
    void shouldNotMatchNullHashedPassword() {
        assertThat(hasher.match("foobar", null)).isFalse();
    }

    @Test
    void shouldNotMatchBadPassword() {
        String password = "password";
        assertThat(hasher.match("badPassword", hasher.hash(password)));
    }

    @Test
    void hashesShouldBeDifferentWithSamePassword() {
        String password = "password";
        String hash1 = hasher.hash(password);
        String hash2 = hasher.hash(password);
        assertThat(hash1).isNotEqualTo(hash2);
    }
}
