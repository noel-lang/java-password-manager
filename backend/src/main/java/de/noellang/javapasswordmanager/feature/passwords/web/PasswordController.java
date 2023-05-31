package de.noellang.javapasswordmanager.feature.passwords.web;


import de.noellang.javapasswordmanager.domain.Password;
import de.noellang.javapasswordmanager.domain.User;
import de.noellang.javapasswordmanager.feature.authentication.repository.UserRepository;
import de.noellang.javapasswordmanager.feature.passwords.repository.PasswordRepository;
import de.noellang.javapasswordmanager.feature.passwords.web.dto.PasswordDto;
import de.noellang.javapasswordmanager.feature.passwords.web.dto.PasswordSaveRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/passwords")
public class PasswordController {

	private final UserRepository userRepository;

	private final PasswordRepository passwordRepository;

	public PasswordController(UserRepository userRepository, PasswordRepository passwordRepository) {
		this.userRepository = userRepository;
		this.passwordRepository = passwordRepository;
	}

	@GetMapping
	public ResponseEntity<List<PasswordDto>> index(Authentication authentication) {
		User user = userRepository.findByUsername(authentication.getName()).orElseThrow(RuntimeException::new);

		List<PasswordDto> passwordDtoList = passwordRepository.findAllByUser(user).stream()
			.map(password -> new PasswordDto(password.getPublicId(), password.getData(), password.getNonce())).toList();

		return ResponseEntity.ok(passwordDtoList);
	}

	@PostMapping
	public ResponseEntity<?> save(@RequestBody @Valid PasswordSaveRequestDto passwordSaveRequestDto, Authentication authentication) {
		User user = userRepository.findByUsername(authentication.getName()).orElseThrow(RuntimeException::new);

		Password password = new Password();

		password.setUser(user);
		password.setNonce(passwordSaveRequestDto.nonce());
		password.setData(passwordSaveRequestDto.data());

		Password savedPassword = passwordRepository.save(password);

		return ResponseEntity.ok(savedPassword);
	}

}
