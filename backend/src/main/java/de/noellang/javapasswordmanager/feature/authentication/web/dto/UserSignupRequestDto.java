package de.noellang.javapasswordmanager.feature.authentication.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserSignupRequestDto(
		@NotNull
		@Size(min = 1, max = 100)
		String username,

		@NotNull
		String hashedPassword,

		@NotNull
		String salt
) {}
