package fr.mnhn.diversity.common.exception;

/**
 * An exception thrown to signal a bad request, but with a well-defined error code and a message
 */
public class FunctionalException extends BadRequestException {
    public enum Code {
        INDICATOR_CATEGORY_NAME_ALREADY_EXISTING("Une catégorie avec ce nom existe déjà"),
        INDICATOR_SLUG_ALREADY_EXISTING("Un indicateur avec ce slug existe déjà"),
        INDICATOR_VALUES_NOT_FOUND("Cet indicateur (ou ses valeurs) n'existe pas dans l'entrepôt"),
        HTTP_ERROR_WHILE_FETCHING_INDICATOR_VALUES("Erreur de communication avec l'entrepôt des indicateurs"),
        UNEXPECTED_ERROR_WHILE_FETCHING_INDICATOR_VALUES("Erreur inattendue lors de la récupération des valeurs. Vérifiez que cet indicateur est au format ettendu dans l'entrepôt."),
        INDICATOR_BIOM_ID_ALREADY_EXISTING("Un indicateur avec ce BIOM ID existe déjà"),
        ECOGESTURE_SLUG_ALREADY_EXISTING("Un éco-geste avec ce slug existe déjà"),
        MEDIA_NAME_ALREADY_EXISTING("Un média avec ce nom existe déjà"),
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
