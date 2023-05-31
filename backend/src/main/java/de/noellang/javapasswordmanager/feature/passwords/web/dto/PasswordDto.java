package de.noellang.javapasswordmanager.feature.passwords.web.dto;

import java.util.UUID;

public record PasswordDto(UUID id, String data, String nonce) {
}
