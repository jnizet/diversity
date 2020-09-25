package fr.mnhn.diversity.admin.security;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

/**
 * Helper class to build a JWT token or extract the claim from a token.
 * @author JB Nizet
 */
@Component
public class JwtHelper {

    static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private final SecretKey key;

    public JwtHelper(SecurityProperties securityProperties) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(securityProperties.getSecretKey()));
    }

    /**
     * Builds a token for the given login
     *
     * @param login - the user ID
     * @return a JWT token as a String
     */
    public String buildToken(String login) {
        return Jwts.builder()
                   .setSubject(login)
                   .setExpiration(Date.from(Instant.now().plus(30, ChronoUnit.DAYS)))
                   .signWith(key)
                   .compact();
    }

    /**
     * Extracts the login from the JWT token
     *
     * @param token - token to analyze
     * @return the login contained in the token
     */
    public String extractLogin(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public static void main(String[] args) {
        System.out.println(Base64.getEncoder().encodeToString(Keys.secretKeyFor(SIGNATURE_ALGORITHM).getEncoded()));
    }
}
