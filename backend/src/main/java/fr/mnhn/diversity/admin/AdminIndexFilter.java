package fr.mnhn.diversity.admin;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet filter used to forward to the admin index.html page for all requests starting by /admin and not targetting
 * static assets
 * @author JB Nizet
 */
public class AdminIndexFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        if (mustForward(request)) {
            request.getRequestDispatcher("/admin/index.html").forward(request, response);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean mustForward(HttpServletRequest request) {
        if (!request.getMethod().equals("GET")) {
            return false;
        }

        String fullUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String uri = fullUri.substring(contextPath.length());

        return !(
            uri.startsWith("/admin/index.html")
                || uri.endsWith(".js")
                || uri.endsWith(".css")
                || uri.endsWith(".ico")
                || uri.endsWith(".png")
                || uri.endsWith(".jpg")
                || uri.endsWith(".gif")
                || uri.endsWith(".eot")
                || uri.endsWith(".svg")
                || uri.endsWith(".woff2")
                || uri.endsWith(".ttf")
                || uri.endsWith(".woff")
                || uri.endsWith(".json"));
    }
}
