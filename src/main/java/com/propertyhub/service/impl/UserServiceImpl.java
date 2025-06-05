package com.propertyhub.service.impl;

import com.propertyhub.dto.user.UserDetailsResponse;
import com.propertyhub.model.User;
import com.propertyhub.repository.UserRepository;
import com.propertyhub.service.UserService;
import com.propertyhub.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsResponse getCurrentUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        User user = userRepository.findByEmail(username) // Assuming email is username
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", username));

        return new UserDetailsResponse(
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getAgencyName(),
            user.getAvatarUrl(),
            user.getRole().name()
        );
    }
}
