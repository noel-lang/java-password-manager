package de.noellang.javapasswordmanager.feature.authentication.web.dto;

public record UserSignupRequestDto(String username, String hashedPassword, String salt) {
}
