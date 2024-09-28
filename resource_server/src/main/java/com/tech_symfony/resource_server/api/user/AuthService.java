package com.tech_symfony.resource_server.api.user;

import com.tech_symfony.resource_server.commonlibrary.constants.MessageCode;
import com.tech_symfony.resource_server.commonlibrary.exception.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new AccessDeniedException(MessageCode.FORBIDDEN));
    }
}
