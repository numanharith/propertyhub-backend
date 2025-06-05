package com.propertyhub.controller;

import com.propertyhub.dto.user.UserDetailsResponse;
import com.propertyhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()") // Ensures only authenticated users can access
    public ResponseEntity<UserDetailsResponse> getCurrentUserDetails() {
        UserDetailsResponse userDetails = userService.getCurrentUserDetails();
        return ResponseEntity.ok(userDetails);
    }
}
