package fr.mnhn.diversity.admin.security;

import fr.mnhn.diversity.common.exception.FunctionalException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used to authenticate
 * @author JB Nizet
 */
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationRestController {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final JwtHelper jwtHelper;

    public AuthenticationRestController(UserRepository userRepository,
                                        PasswordHasher passwordHasher,
                                        JwtHelper jwtHelper) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.jwtHelper = jwtHelper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticatedUserDTO authenticate(@Validated @RequestBody CredentialsCommandDTO credentials) {
        User user = userRepository.findByLogin(credentials.getLogin()).orElseThrow(
            () -> new FunctionalException(FunctionalException.Code.AUTHENTICATION_FAILED)
        );
        if (!passwordHasher.match(credentials.getPassword(), user.getHashedPassword())) {
            throw new FunctionalException(FunctionalException.Code.AUTHENTICATION_FAILED);
        }

        return new AuthenticatedUserDTO(user.getId(), user.getLogin(), jwtHelper.buildToken(user.getLogin()));
    }
}
