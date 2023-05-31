package de.noellang.javapasswordmanager.feature.authentication.config;

import de.noellang.javapasswordmanager.domain.User;
import de.noellang.javapasswordmanager.feature.authentication.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private static final Logger LOG = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

	private final UserRepository userRepository;

	public CustomAuthenticationProvider(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String hashedPasswordFromUser = authentication.getCredentials().toString();

		User user = userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
		String hashedPasswordFromDatabase = user.getHashedPassword();

		if (hashedPasswordFromUser.equals(hashedPasswordFromDatabase)) {
			return new UsernamePasswordAuthenticationToken(username, hashedPasswordFromDatabase, new ArrayList<>());
		}

		throw new BadCredentialsException("Authentication failed");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
