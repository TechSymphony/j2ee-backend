package com.tech_symfony.auth_server.service;

import com.tech_symfony.auth_server.model.MyUserDetails;
import com.tech_symfony.auth_server.model.User;
import com.tech_symfony.auth_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        log.trace("Load user {}", oAuth2UserRequest);
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        MyUserDetails myUserDetails = new MyUserDetails(processOAuth2User(oAuth2UserRequest, oAuth2User), null, oAuth2User.getAttributes());
        return myUserDetails;
    }

    private User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {

        User userFromOauth2 = User.builder()
                .email(oAuth2User.getAttributes().get("email").toString())
                .username(oAuth2User.getAttributes().get("email").toString())
                .password("password")
                .enabled(true)
                .fullName(oAuth2User.getAttributes().get("name").toString())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
//        userFromOauth2.setProvider(Provider.GOOGLE);
//        userFromOauth2.setProviderId(oAuth2UserRequest.getClientRegistration().getRegistrationId());

//        userFromOauth2.setAvatar(oAuth2User.getAttributes().get("picture").toString());
        log.info("User info is {}", userFromOauth2);

        User user = userRepository.findByUsername(userFromOauth2.getEmail());
        if (user == null) {
            return registerNewUser(userFromOauth2);
        } else {
            return updateExistingUser(user, userFromOauth2);
        }
    }

    private User registerNewUser(User user) {

        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, User user) {
        existingUser.setFullName(user.getFullName());
//        existingUser.setAvatar(user.getAvatar());
        return userRepository.save(existingUser);
    }
}
