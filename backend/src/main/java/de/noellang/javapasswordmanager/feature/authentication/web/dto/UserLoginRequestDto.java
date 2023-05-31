package de.noellang.javapasswordmanager.feature.authentication.web.dto;

import org.springframework.lang.NonNull;

public record UserLoginRequestDto(@NonNull String username, @NonNull String hashedPassword) {
}
