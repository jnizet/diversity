package fr.mnhn.diversity.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Tests for {@link AdminIndexFilter}
 * @author JB Nizet
 */
class AdminIndexFilterTest {

    private AdminIndexFilter filter;

    @BeforeEach
    void prepare() {
        filter = new AdminIndexFilter();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "/admin/index.html",
        "/admin/favicon.ico",
        "/admin/assets/foo.png",
        "/admin/assets/foo.jpg",
        "/admin/assets/foo.svg",
        "/admin/assets/foo.gif",
        "/admin/assets/font.eot",
        "/admin/assets/font.ttf",
        "/admin/assets/font.woff",
        "/admin/assets/font.woff2",
        "/admin/script.js",
        "/admin/style.css",
        "/admin/favicon.ico",
        "/admin/i18n.json"
    })
    void shouldNotForwardForGetRequestWithUri(String uri) throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", uri);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(any(), any());
        assertThat(response.getForwardedUrl()).isNull();
    }

    @Test
    void shouldNotForwardForNonGetRequest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/admin/foo");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(any(), any());
        assertThat(response.getForwardedUrl()).isNull();
    }

    @Test
    void shouldForwardForGetRequestToFrontendUri() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/foo");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(chain, never()).doFilter(any(), any());
        assertThat(response.getForwardedUrl()).isEqualTo("/admin/index.html");
    }
}
