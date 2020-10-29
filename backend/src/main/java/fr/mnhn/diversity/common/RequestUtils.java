package fr.mnhn.diversity.common;

import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;

/**
 * HttpServletRequest-related utilities
 */
public final class RequestUtils {
    private RequestUtils() {
    }

    /**
     * Returns the base URL of the given request (including the context path, if any). For example, if
     * the request is for <code>https://outremer.mnhn.fr/territoires?foo=bar</code>, this method returns
     * <code>https://outremer.mnhn.fr</code>
     */
    public static String absoluteBaseUrl(HttpServletRequest request) {
        URI uri = URI.create(request.getRequestURL().toString());
        try {
            return new URI(request.getScheme(),
                           null,
                           uri.getHost(),
                           uri.getPort(),
                           request.getContextPath(),
                           null,
                           null).toString();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }
}
