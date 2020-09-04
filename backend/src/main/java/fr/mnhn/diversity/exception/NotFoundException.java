package fr.mnhn.diversity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown to signal a not found resource
 * @author JB Nizet
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
}
