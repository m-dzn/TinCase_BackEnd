package com.appleisle.tincase.service;

import com.appleisle.tincase.domain.user.User;
import com.appleisle.tincase.domain.user.UserPrincipal;
import com.appleisle.tincase.enumclass.OAuthProvider;
import com.appleisle.tincase.enumclass.RoleName;
import com.appleisle.tincase.exception.OAuth2EmailEmptyException;
import com.appleisle.tincase.exception.OAuth2ExistsException;
import com.appleisle.tincase.repository.user.UserRepository;
import com.appleisle.tincase.security.oauth2.userinfo.OAuth2UserInfo;
import com.appleisle.tincase.security.oauth2.userinfo.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    @Transactional
    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getUserInfo(
                userRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes());

        // email 길이가 0이면 에러 메세지 출력
        if (!StringUtils.hasLength(userInfo.getEmail())) {
            throw new OAuth2EmailEmptyException();
        }

        Optional<User> userOptional = userRepository.findByEmail(userInfo.getEmail());

        User user;
        if (!userOptional.isPresent()) {   // 해당 이메일로 가입된 User가 없으면 가입 처리
            user = join(userRequest, userInfo);
        } else {
            user = userOptional.get();

            // 해당 이메일로 가입된 User가 있지만 SNS Provider가 다를 경우 에러 메세지 출력 
            if (!isSameProvider(user, userRequest)) {
                String provider = user.getProvider().toString();
                throw new OAuth2ExistsException(provider);
            }

            // 같은 Provider일 경우 정보 업데이트
            user = updateUser(user, userInfo);
        }

        // join 또는 update 처리 후 UserDetails 객체로 만들어 loadUser()로 반환
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private boolean isSameProvider(User user, OAuth2UserRequest userRequest) {
        String userProvider = user.getProvider().toString();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        return userProvider.equals(registrationId);
    }

    @Transactional
    private User join(OAuth2UserRequest userRequest, OAuth2UserInfo userInfo) {
        User user = User.builder()
                .email(userInfo.getEmail())
                .nickname(userInfo.getName())
                .build();
        user.setAvatar(userInfo.getAvatar());
        user.setProvider(OAuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId()));

        User newUser = userRepository.save(user);

        return newUser;
    }

    private User updateUser(User existingUser, OAuth2UserInfo userInfo) {
        return userRepository.save(existingUser);
    }

}
