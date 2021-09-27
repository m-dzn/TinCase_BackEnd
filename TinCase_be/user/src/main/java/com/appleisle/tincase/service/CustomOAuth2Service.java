package com.appleisle.tincase.service;

import com.appleisle.tincase.domain.user.User;
import com.appleisle.tincase.domain.user.UserPrincipal;
import com.appleisle.tincase.exception.OAuth2ProcessingException;
import com.appleisle.tincase.repository.UserRepository;
import com.appleisle.tincase.security.oauth2.OAuth2UserInfo;
import com.appleisle.tincase.security.oauth2.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2Service extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getUserInfo(
                userRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes());

        if (StringUtils.isEmpty(userInfo.getEmail())) {
            throw new OAuth2ProcessingException("");
        }

        Optional<User> userOptional = userRepository.findByEmail(userInfo.getEmail());

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();

            if (user == null) {
                throw new OAuth2ProcessingException("");
            }
        } else {
            user = register(userRequest, userInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User register(OAuth2UserRequest userRequest, OAuth2UserInfo userInfo) {
        User user = userInfo.toEntity();

        return userRepository.save(user);
    }

    private User update(User existingUser, OAuth2UserInfo userInfo) {
        return userRepository.save(existingUser);
    }

}
