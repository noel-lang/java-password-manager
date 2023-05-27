package de.noellang.javapasswordmanager.feature.authentication.web;

import de.noellang.javapasswordmanager.domain.User;
import de.noellang.javapasswordmanager.feature.authentication.repository.UserRepository;
import de.noellang.javapasswordmanager.feature.authentication.web.dto.UserSignupRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/authentication")
public class AuthenticationController {

	private final UserRepository userRepository;

	public AuthenticationController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostMapping(value = "/register")
	public ResponseEntity<?> register(@RequestBody @Valid UserSignupRequestDto userSignupRequestDto) {
		userRepository.findByUsername(userSignupRequestDto.username()).ifPresent((user) -> {
			throw new RuntimeException("Dieser Nutzer existiert bereits.");
		});
		
		User user = new User();

		user.setUsername(userSignupRequestDto.username());
		user.setHashedPassword(userSignupRequestDto.hashedPassword());
		user.setSalt(userSignupRequestDto.salt());

		User savedUser = userRepository.save(user);

		return ResponseEntity.ok(savedUser);
	}

}
