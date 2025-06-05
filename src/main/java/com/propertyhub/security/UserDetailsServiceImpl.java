package com.propertyhub.security;

import com.propertyhub.model.User;
import com.propertyhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        // Allow login with either email or username
        User user = userRepository.findByEmail(emailOrUsername)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + emailOrUsername));

        // Get authorities (roles)
        Collection<? extends GrantedAuthority> authorities = 
            Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
            
        // Note: Spring Security's UserDetails expects username, so we pass email here as the primary identifier
        // Ensure your JwtUtil and AuthenticationManager are configured to handle email as the "username" field.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // Using email as the "username" for UserDetails
                user.getPasswordHash(),
                authorities
        );
    }
}
