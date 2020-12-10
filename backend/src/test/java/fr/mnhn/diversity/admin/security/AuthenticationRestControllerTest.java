package fr.mnhn.diversity.admin.security;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.mnhn.diversity.ControllerTest;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * MVC tests for {@link AuthenticationRestController}
 * @author JB Nizet
 */
@WebMvcTest(AuthenticationRestController.class)
class AuthenticationRestControllerTest extends ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository mockUserRepository;

    @MockBean
    private PasswordHasher mockPasswordHasher;

    @MockBean
    private JwtHelper mockJwtHelper;

    @Test
    void shouldAuthenticateIfCorrectCredentials() throws Exception {
        User user = new User(42L, "gregoria", "hash");
        CredentialsCommandDTO credentials = new CredentialsCommandDTO(user.getLogin(), "secr3t");
        when(mockUserRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        when(mockPasswordHasher.match("secr3t", "hash")).thenReturn(true);
        when(mockJwtHelper.buildToken(user.getLogin())).thenReturn("token");

        mockMvc.perform(post("/api/authentication")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(credentials)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(user.getId()))
               .andExpect(jsonPath("$.login").value(user.getLogin()))
               .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    void shouldFailIfUnknownUser() throws Exception {
        User user = new User(42L, "gregoria", "hash");
        CredentialsCommandDTO credentials = new CredentialsCommandDTO(user.getLogin(), "secr3t");
        when(mockUserRepository.findByLogin(user.getLogin())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/authentication")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(credentials)))
               .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailIfInvalidCredentials() throws Exception {
        User user = new User(42L, "gregoria", "hash");
        CredentialsCommandDTO credentials = new CredentialsCommandDTO(user.getLogin(), "secr3t");
        when(mockUserRepository.findByLogin(user.getLogin())).thenReturn(Optional.empty());
        when(mockPasswordHasher.match("secr3t", "hash")).thenReturn(false);

        mockMvc.perform(post("/api/authentication")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(credentials)))
               .andExpect(status().isBadRequest());
    }
}
