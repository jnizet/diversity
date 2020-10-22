package fr.mnhn.diversity.contact;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests for {@link ContactController}
 * @author JB Nizet
 */
@WebMvcTest(ContactController.class)
@Import(ContactConfig.class)
class ContactControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Mailer mockMailer;

    @Autowired
    private ContactProperties contactProperties;

    @Test
    void shouldSendMessage() throws Exception {
        MessageCommand command = new MessageCommand("john@mail.com", "Test subject", "Hello");
        mockMvc.perform(post("/messages")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(command)))
               .andExpect(status().isCreated());

        verify(mockMailer).send(
            new MailMessage("john@mail.com", contactProperties.getEmail(), contactProperties.getSubject() + " - Test subject", "Hello")
        );
    }

    @Test
    void shouldNotSendMessageIfInvalidFrom() throws Exception {
        MessageCommand command = new MessageCommand("john", "Test subject", "Hello");
        mockMvc.perform(post("/messages")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(command)))
               .andExpect(status().isBadRequest());

        verify(mockMailer, never()).send(any());
    }

    @Test
    void shouldNotSendMessageIfInvalidSubject() throws Exception {
        MessageCommand command = new MessageCommand("john@mail.com", "  ", "test");
        mockMvc.perform(post("/messages")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(command)))
               .andExpect(status().isBadRequest());

        verify(mockMailer, never()).send(any());
    }

    @Test
    void shouldNotSendMessageIfInvalidBody() throws Exception {
        MessageCommand command = new MessageCommand("john@mail.com", "Test subject", "A".repeat(701));
        mockMvc.perform(post("/messages")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(command)))
               .andExpect(status().isBadRequest());

        verify(mockMailer, never()).send(any());
    }
}
