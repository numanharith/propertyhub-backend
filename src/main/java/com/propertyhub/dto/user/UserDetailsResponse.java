package com.propertyhub.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponse {
    private UUID id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String agencyName;
    private String avatarUrl;
    private String role;
}
