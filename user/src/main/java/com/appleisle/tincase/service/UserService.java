package com.appleisle.tincase.service;

import com.appleisle.tincase.domain.user.User;
import com.appleisle.tincase.dto.request.JoinForm;
import com.appleisle.tincase.dto.request.UserUpdateForm;
import com.appleisle.tincase.dto.response.UserSummary;
import com.appleisle.tincase.exception.EmailExistsException;
import com.appleisle.tincase.exception.ResourceNotFoundException;
import com.appleisle.tincase.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void join(JoinForm joinForm) {
        if (userRepository.existsByEmail(joinForm.getEmail())) {
            throw new EmailExistsException(joinForm.getEmail());
        }

        User newUser = User.builder()
                .email(joinForm.getEmail())
                .password(passwordEncoder.encode(joinForm.getPassword()))
                .nickname(joinForm.getNickname())
                .build();
        newUser.setAddress(joinForm.getAddress(), joinForm.getAddressDetail());

        userRepository.save(newUser);
    }

    @Transactional
    public UserSummary updateUser(Long id, UserUpdateForm request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setNickname(request.getNickname());
                    user.setAddress(request.getAddress(), request.getAddressDetail());
                    return UserSummary.of(user);
                })
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

}
