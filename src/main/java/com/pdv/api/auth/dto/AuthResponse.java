package com.pdv.api.auth.dto;

public record AuthResponse(
        String token,
        String username,
        String role,
        String fullName,
        String email
) {
}
