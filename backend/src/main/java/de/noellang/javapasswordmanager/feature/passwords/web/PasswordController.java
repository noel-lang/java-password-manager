package de.noellang.javapasswordmanager.feature.passwords.web;


import de.noellang.javapasswordmanager.domain.Password;
import de.noellang.javapasswordmanager.feature.authentication.repository.UserRepository;
import de.noellang.javapasswordmanager.feature.passwords.repository.PasswordRepository;
import de.noellang.javapasswordmanager.feature.passwords.web.dto.PasswordSaveRequestDto;
import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/passwords")
public class PasswordController {

    private final UserRepository userRepository;

    private final PasswordRepository passwordRepository;

    public PasswordController(UserRepository userRepository, PasswordRepository passwordRepository) {
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid PasswordSaveRequestDto passwordSaveRequestDto, Authentication authentication) {
        de.noellang.javapasswordmanager.domain.User savedUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(RuntimeException::new);

        Password password = new Password();

        password.setUser(savedUser);
        password.setNonce(passwordSaveRequestDto.nonce());
        password.setData(passwordSaveRequestDto.data());

        Password savedPassword = passwordRepository.save(password);

        return ResponseEntity.ok(savedPassword);
    }

}
