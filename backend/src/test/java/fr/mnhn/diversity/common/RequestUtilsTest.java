package fr.mnhn.diversity.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class RequestUtilsTest {
    @Test
    void shouldReturnAbsoluteBaseUrl() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/territoires?foo=bar");
        request.setScheme("https");
        request.setServerName("outremer.mnhn.fr");
        request.setServerPort(443);
        request.setContextPath("");
        assertThat(RequestUtils.absoluteBaseUrl(request)).isEqualTo("https://outremer.mnhn.fr");

        request.setContextPath("/foo");

        assertThat(RequestUtils.absoluteBaseUrl(request)).isEqualTo("https://outremer.mnhn.fr/foo");
    }
}
