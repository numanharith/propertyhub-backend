package com.propertyhub.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String jwtToken;
    private UUID userId;
    private String username;
    private String role;
    // Could include token type (e.g., "Bearer") and expiration time
}
