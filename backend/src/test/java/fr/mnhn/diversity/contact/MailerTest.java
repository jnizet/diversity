package fr.mnhn.diversity.contact;

import static org.mockito.Mockito.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * Unit tests for {@link Mailer}
 * @author JB Nizet
 */
class MailerTest {
    @Test
    void shouldSendMimeMessage() throws MessagingException {
        JavaMailSender mockJavaMailSender = mock(JavaMailSender.class);
        MimeMessage mockMimeMessage = mock(MimeMessage.class);
        when(mockJavaMailSender.createMimeMessage()).thenReturn(mockMimeMessage);

        MimeMessageHelper mockHelper = mock(MimeMessageHelper.class);
        Mailer mailer = new TestMailer(mockJavaMailSender, mockHelper);

        MailMessage mailMessage = new MailMessage(
            "john@mail.com",
            "contact@mnhn.com",
            "Hello",
            "This is plain text"
        );

        mailer.send(mailMessage);

        verify(mockJavaMailSender).send(mockMimeMessage);
        verify(mockHelper).setFrom(mailMessage.getFrom());
        verify(mockHelper).setTo(mailMessage.getTo());
        verify(mockHelper).setSubject(mailMessage.getSubject());
        verify(mockHelper).setText(mailMessage.getPlainText());
    }

    private static class TestMailer extends Mailer {
        private final MimeMessageHelper mimeMessageHelper;

        public TestMailer(JavaMailSender javaMailSender, MimeMessageHelper mimeMessageHelper) {
            super(javaMailSender);
            this.mimeMessageHelper = mimeMessageHelper;
        }

        @Override
        protected MimeMessageHelper createHelper(MimeMessage mimeMessage) {
            return mimeMessageHelper;
        }
    }

}
