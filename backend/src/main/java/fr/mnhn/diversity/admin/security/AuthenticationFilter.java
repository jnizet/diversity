package fr.mnhn.diversity.admin.security;


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * Servlet filter used to check that the user is authenticated, on all API URLs except `/api/authentication`.
 *
 * This filter is registered by {@link fr.mnhn.diversity.admin.security.SecurityConfig}.
 *
 * @author JB Nizet
 */
public class AuthenticationFilter implements Filter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String KEY_PREFIX = "Key ";

    private final JwtHelper jwtHelper;
    private final UserRepository userRepository;
    private final ApiKeyRepository apiKeyRepository;

    public AuthenticationFilter(JwtHelper jwtHelper, UserRepository userRepository,
        ApiKeyRepository apiKeyRepository) {
        this.jwtHelper = jwtHelper;
        this.userRepository = userRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String host = request.getRemoteHost();
        String key = extractKey(request);

        if (key != null && isReadOnlyApiFromServerRequest(request) && apiKeyRepository.existsByHostAndKey(host, key)) {
            chain.doFilter(request, response);
            return;
        }
        String login = extractLoginFromToken(request);
        if (isProtectedApiRequest(request) && (login == null || !userRepository.existsByLogin(login))) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized origin: " + host);
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isProtectedApiRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/api") && !requestURI.equals("/api/authentication");
    }

    private boolean isReadOnlyApiFromServerRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/api") && !requestURI.equals("/api/authentication") && request.getMethod().equals("GET");
    }

    private String extractLoginFromToken(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null) {
            return null;
        }

        try {
            return jwtHelper.extractLogin(token);
        } catch (Exception e) {
            return null;
        }
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            return null;
        }
        if (!header.startsWith(BEARER_PREFIX)) {
            return null;
        }

        return header.substring(BEARER_PREFIX.length()).trim();
    }

    private String extractKey(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            return null;
        }
        if (!header.startsWith(KEY_PREFIX)) {
            return null;
        }

        return header.substring(KEY_PREFIX.length()).trim();
    }
}
