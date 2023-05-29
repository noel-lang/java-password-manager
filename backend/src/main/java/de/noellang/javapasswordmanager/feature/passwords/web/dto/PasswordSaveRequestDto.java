package de.noellang.javapasswordmanager.feature.passwords.web.dto;

import org.springframework.lang.NonNull;

public record PasswordSaveRequestDto(
        @NonNull
        String nonce,

        @NonNull
        String data
) {}
