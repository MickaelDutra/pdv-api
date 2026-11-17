package com.pdv.api.auth.dto;

import com.pdv.api.auth.entity.Role;

public record AuthResponse(
        String token,
        String username,
        Role role,
        String fullName,
        String email
) {
}
