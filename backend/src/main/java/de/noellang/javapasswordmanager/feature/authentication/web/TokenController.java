package de.noellang.javapasswordmanager.feature.authentication.web;

import de.noellang.javapasswordmanager.feature.authentication.web.dto.UserLoginRequestDto;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/authentication")
public class TokenController {

	private final JwtEncoder jwtEncoder;

	private final AuthenticationManager authenticationManager;

	public TokenController(JwtEncoder jwtEncoder, AuthenticationManager authenticationManager) {
		this.jwtEncoder = jwtEncoder;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/token")
	public String token(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userLoginRequestDto.username(), userLoginRequestDto.hashedPassword());
		Authentication authentication = authenticationManager.authenticate(token);

		Instant now = Instant.now();
		long expiry = 36000L;

		String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(" "));

		JwtClaimsSet claims = JwtClaimsSet.builder().issuer("self").issuedAt(now).expiresAt(now.plusSeconds(expiry))
			.subject(authentication.getName()).claim("scope", scope).build();

		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}

}
