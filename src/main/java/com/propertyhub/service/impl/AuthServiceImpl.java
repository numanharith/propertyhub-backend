package com.propertyhub.service.impl;

import com.propertyhub.dto.auth.AuthResponse;
import com.propertyhub.dto.auth.UserLoginRequest;
import com.propertyhub.dto.auth.UserRegistrationRequest;
import com.propertyhub.model.User;
import com.propertyhub.model.Role;
import com.propertyhub.repository.UserRepository;
import com.propertyhub.security.jwt.JwtUtil;
import com.propertyhub.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public String registerUser(UserRegistrationRequest registrationRequest) {
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!"); // Or custom exception
        }

        User user = new User();
        user.setFullName(registrationRequest.getFirstName() + " " + registrationRequest.getLastName());
        user.setEmail(registrationRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registrationRequest.getPassword()));

        // Default role, e.g. ROLE_SEEKER. This could be configurable.
        // For PropertyHub, new users might be ROLE_SEEKER by default.
        // They might need a separate process or admin approval to become ROLE_LISTER.
        // For now, let's assign ROLE_SEEKER or ROLE_LISTER for simplicity during registration for testing.
        // In a real app, this would be more nuanced. Let's assume default is seeker.
        user.setRole(Role.ROLE_SEEKER); 
        // If they need to be listers:
        // user.setRole(Role.ROLE_LISTER);

        userRepository.save(user);
        return "User registered successfully!";
    }

    @Override
    public AuthResponse loginUser(UserLoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails.getUsername());

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found after authentication")); // Should not happen

        return new AuthResponse(jwt, user.getId(), user.getEmail(), user.getRole().name());
    }
}
