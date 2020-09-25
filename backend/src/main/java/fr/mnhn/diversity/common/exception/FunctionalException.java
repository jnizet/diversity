package fr.mnhn.diversity.common.exception;

/**
 * An exception thrown to signal a bad request, but with a well-defined error code and a message
 */
public class FunctionalException extends BadRequestException {
    public enum Code {
        INDICATOR_CATEGORY_NAME_ALREADY_EXISTING("Une catégorie avec ce nom existe déjà"),
        INDICATOR_SLUG_ALREADY_EXISTING("Un indicateur avec ce slug existe déjà"),
        INDICATOR_VALUES_NOT_FOUND("Cet identifiant BIOM ne retourne pas de valeurs"),
        INDICATOR_BIOM_ID_ALREADY_EXISTING("Un indicateur avec ce BIOM ID existe déjà"),
        AUTHENTICATION_FAILED("L'identification a échoué");

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
