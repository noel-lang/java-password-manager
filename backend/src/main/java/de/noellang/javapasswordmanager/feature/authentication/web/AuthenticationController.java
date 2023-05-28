package de.noellang.javapasswordmanager.feature.authentication.web;

import de.noellang.javapasswordmanager.domain.User;
import de.noellang.javapasswordmanager.feature.authentication.repository.UserRepository;
import de.noellang.javapasswordmanager.feature.authentication.web.dto.UserSaltResponseDto;
import de.noellang.javapasswordmanager.feature.authentication.web.dto.UserSignupRequestDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/authentication")
public class AuthenticationController {

	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

	private final UserRepository userRepository;

	public AuthenticationController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostMapping(value = "/register")
	public ResponseEntity<?> register(@RequestBody @Valid UserSignupRequestDto userSignupRequestDto) {
		userRepository.findByUsername(userSignupRequestDto.username()).ifPresent((user) -> {
			throw new RuntimeException("Dieser Nutzer existiert bereits.");
		});

		LOG.info("Register, Username: {}", userSignupRequestDto.username());
		LOG.info("Register, Hashed Password: {}", userSignupRequestDto.hashedPassword());
		LOG.info("Register, Salt: {}", userSignupRequestDto.salt());

		User user = new User();

		user.setUsername(userSignupRequestDto.username());
		user.setHashedPassword(userSignupRequestDto.hashedPassword());
		user.setSalt(userSignupRequestDto.salt());

		User savedUser = userRepository.save(user);

		return ResponseEntity.ok(savedUser);
	}

	@GetMapping(value = "/{username}/salt")
	public ResponseEntity<UserSaltResponseDto> retrieveSalt(@PathVariable("username") String username) {
		User user = userRepository.findByUsername(username).orElseThrow(RuntimeException::new);

		LOG.info("Retrieve Salt: {}", user.getSalt());

		return ResponseEntity.ok(new UserSaltResponseDto(user.getSalt()));
	}

}
