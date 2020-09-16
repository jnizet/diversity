package fr.mnhn.diversity.contact;

import java.util.Objects;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

/**
 * JSON command sent to send a contact message
 * @author JB Nizet
 */
public final class MessageCommand {
    @NotBlank
    @Email
    private final String from;

    @NotBlank
    @Length(max = 700)
    private final String body;

    public MessageCommand(@JsonProperty("from") String from,
                          @JsonProperty("body")  String body) {
        this.from = from;
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public String getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageCommand)) {
            return false;
        }
        MessageCommand that = (MessageCommand) o;
        return Objects.equals(from, that.from) &&
            Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, body);
    }

    @Override
    public String toString() {
        return "MessageCommand{" +
            "from='" + from + '\'' +
            ", body='" + body + '\'' +
            '}';
    }
}
