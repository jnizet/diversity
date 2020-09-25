package fr.mnhn.diversity.admin.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Base64;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JwtHelper}
 * @author JB Nizet
 */
class JwtHelperTest {
    private JwtHelper jwtHelper;

    @BeforeEach
    void prepare() {
        jwtHelper = new JwtHelper(
            new SecurityProperties(Base64.getEncoder().encodeToString(Keys.secretKeyFor(JwtHelper.SIGNATURE_ALGORITHM).getEncoded()))
        );
    }

    @Test
    void shouldBuildAndParseToken() {
        String login = "gregoria";

        String token = jwtHelper.buildToken(login);

        assertThat(token.length()).isGreaterThan(20);
        assertThat(jwtHelper.extractLogin(token)).isEqualTo(login);
    }
}
