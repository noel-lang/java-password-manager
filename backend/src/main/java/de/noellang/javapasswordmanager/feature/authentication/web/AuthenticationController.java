package de.noellang.javapasswordmanager.feature.authentication.web;

import de.noellang.javapasswordmanager.domain.User;
import de.noellang.javapasswordmanager.feature.authentication.repository.UserRepository;
import de.noellang.javapasswordmanager.feature.authentication.web.dto.UserSignupRequestDto;
import jakarta.annotation.security.PermitAll;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/v1.0/authentication")
@PermitAll
public class AuthenticationController {

	private final UserRepository userRepository;

	public AuthenticationController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostMapping(value = "/register")
	@PermitAll
	public ResponseEntity<?> register(@RequestBody UserSignupRequestDto userSignupRequestDto) {
		User user = new User();

		user.setUsername(userSignupRequestDto.username());
		user.setHashedPassword(userSignupRequestDto.hashedPassword());
		user.setSalt(userSignupRequestDto.salt());
		user.setPublicId(UUID.randomUUID());

		User savedUser = userRepository.save(user);

		return ResponseEntity.ok(savedUser);
	}

}
