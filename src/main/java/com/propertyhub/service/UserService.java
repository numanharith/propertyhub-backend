package com.propertyhub.service;

import com.propertyhub.dto.user.UserDetailsResponse;

public interface UserService {
    /**
     * Retrieves details of the currently authenticated user.
     * @return UserDetailsResponse DTO.
     */
    UserDetailsResponse getCurrentUserDetails();
}
