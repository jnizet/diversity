package fr.mnhn.diversity.common.exception;

/**
 * An exception thrown to signal a bad request, but with a well-defined error code and a message
 */
public class FunctionalException extends BadRequestException {
    public enum Code {
        INDICATOR_CATEGORY_NAME_ALREADY_EXISTING("Une catégorie avec ce nom existe déjà");

        private final String message;

        Code(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    private final Code code;

    public FunctionalException(Code code) {
        super(code.getMessage());
        this.code = code;
    }

    public Code getCode() {
        return code;
    }
}
