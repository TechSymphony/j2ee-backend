package com.tech_symfony.auth_server.service;

import com.tech_symfony.auth_server.model.MyUserDetails;
import com.tech_symfony.auth_server.model.User;
import com.tech_symfony.auth_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new MyUserDetails(user, roleService.extractAuthoritiesFromRole(user.getRole()));
    }

}
