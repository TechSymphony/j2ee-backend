package com.tech_symfony.resource_server.api.user;

import com.tech_symfony.resource_server.commonlibrary.constants.MessageCode;
import com.tech_symfony.resource_server.commonlibrary.exception.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new AccessDeniedException(MessageCode.FORBIDDEN));
    }
}
