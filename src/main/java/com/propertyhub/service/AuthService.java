package com.propertyhub.service;

import com.propertyhub.dto.auth.AuthResponse;
import com.propertyhub.dto.auth.UserLoginRequest;
import com.propertyhub.dto.auth.UserRegistrationRequest;

public interface AuthService {
    /**
     * Registers a new user.
     * @param registrationRequest DTO containing user registration details.
     * @return A message indicating success or failure.
     */
    String registerUser(UserRegistrationRequest registrationRequest);

    /**
     * Authenticates a user and returns a JWT.
     * @param loginRequest DTO containing user login credentials.
     * @return AuthResponse DTO containing the JWT and user details.
     */
    AuthResponse loginUser(UserLoginRequest loginRequest);
}
