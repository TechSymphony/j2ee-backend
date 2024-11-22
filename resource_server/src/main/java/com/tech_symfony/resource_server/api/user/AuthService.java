package com.tech_symfony.resource_server.api.user;

import com.tech_symfony.resource_server.commonlibrary.constants.MessageCode;
import com.tech_symfony.resource_server.commonlibrary.exception.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    /**
     * Get current user authenticated
     *
     * @return User
     */
    public User getCurrentUserAuthenticated() {
        return this.getCurrentUserAuthenticatedWithoutHandlingException()
                .orElseThrow(() -> new AccessDeniedException(MessageCode.FORBIDDEN));
    }

    /**
     * Get current user authenticated but having no exception handling
     *
     * @return User
     */
    public Optional<User> getCurrentUserAuthenticatedWithoutHandlingException() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username);
    }

    public User getCurrentUserWithSocketById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new AccessDeniedException(MessageCode.FORBIDDEN));
    }
}