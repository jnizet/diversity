package fr.mnhn.diversity.contact;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller used to send contact emails
 * @author JB Nizet
 */
@RestController
@RequestMapping("/messages")
public class ContactController {
    private final Mailer mailer;
    private final ContactProperties contactProperties;

    public ContactController(Mailer mailer, ContactProperties contactProperties) {
        this.mailer = mailer;
        this.contactProperties = contactProperties;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void sendMessage(@Validated @RequestBody MessageCommand command) {
        mailer.send(new MailMessage(command.getFrom(), contactProperties.getEmail(), contactProperties.getSubject(), command.getBody()));
    }
}
