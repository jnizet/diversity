package fr.mnhn.diversity.admin.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Unit and MVC tests for {@link AuthenticationFilter}
 * @author JB Nizet
 */
@WebMvcTest(SecurityConfig.class)
@MockBean(value = {UserRepository.class, JwtHelper.class})
class AuthenticationFilterTest {

    private JwtHelper mockJwtHelper;
    private UserRepository mockUserRepository;
    private FilterChain mockFilterChain;

    private AuthenticationFilter filter;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void prepare() {
        mockJwtHelper = mock(JwtHelper.class);
        mockUserRepository = mock(UserRepository.class);
        mockFilterChain = mock(FilterChain.class);

        filter = new AuthenticationFilter(mockJwtHelper, mockUserRepository);
    }

    @Test
    void  shouldRejectIfNoHeader() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/indicators");

        shouldRejectWithUnauthorized(request);
    }

    @Test
    void shouldAcceptIfNoHeaderForApiAuthenticationRequest() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/authentication");

        shouldAccept(request);
    }

    @Test
    void shouldRejectIfHeaderWithNoBearerPrefix() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/indicators");
        request.addHeader(HttpHeaders.AUTHORIZATION, "hello world");

        shouldRejectWithUnauthorized(request);
    }

    @Test
    void shouldRejectIfHeaderWithBadToken() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/indicators");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer hello");

        when(mockJwtHelper.extractLogin("hello")).thenThrow(new JwtException("invalid"));

        shouldRejectWithUnauthorized(request);
    }

    @Test
    void shouldRejectIfHeaderWithTokenWithUnknownUser() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/indicators");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer hello");

        when(mockJwtHelper.extractLogin("hello")).thenThrow(new JwtException("unknown"));
        when(mockUserRepository.existsByLogin("unknown")).thenReturn(false);

        shouldRejectWithUnauthorized(request);
    }

    @Test
    void shouldGetUnauthorizedError() throws Exception {
        mockMvc.perform(get("/api/indicator-categories"))
               .andExpect(status().isUnauthorized());
    }

    private void shouldRejectWithUnauthorized(HttpServletRequest request) throws IOException, ServletException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, mockFilterChain);

        verify(mockFilterChain, never()).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void shouldAccept(HttpServletRequest request) throws IOException, ServletException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, mockFilterChain);

        verify(mockFilterChain).doFilter(request, response);
    }
}
