package fr.mnhn.diversity.contact;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * Component that offers a higher-level abstraction than Spring's MimeMessageHelper in order to make it testable
 * @author JB Nizet
 */
@Component
public class Mailer {
    private final JavaMailSender mailSender;

    public Mailer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends an email message
     */
    public void send(MailMessage message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = createHelper(mimeMessage);
            helper.setFrom(message.getFrom());
            helper.setTo(message.getTo());
            helper.setSubject(message.getSubject());
            helper.setText(message.getPlainText());

            mailSender.send(mimeMessage);
        }
        catch (MessagingException e) {
            throw new IllegalStateException(e);
        }
    }

    protected MimeMessageHelper createHelper(MimeMessage mimeMessage) throws MessagingException {
        return new MimeMessageHelper(mimeMessage, true);
    }
}
