package fr.mnhn.diversity.contact;

import java.util.Objects;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for contact
 * @author JB Nizet
 */
@ConfigurationProperties("diversity.contact")
@Validated
public class ContactProperties {

    /**
     * The email address where contact messages received by the application are sent
     */
    @NotNull
    @Email
    private String email;

    /**
     * The subject of the emails sent when a contact message is received
     */
    @NotBlank
    private String subject;

    public ContactProperties() {
    }

    public ContactProperties(@NotNull @Email String email, @NotBlank String subject) {
        this.email = email;
        this.subject = subject;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactProperties)) {
            return false;
        }
        ContactProperties that = (ContactProperties) o;
        return Objects.equals(email, that.email) &&
            Objects.equals(subject, that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, subject);
    }

    @Override
    public String toString() {
        return "ContactProperties{" +
            "email='" + email + '\'' +
            ", subject='" + subject + '\'' +
            '}';
    }
}
