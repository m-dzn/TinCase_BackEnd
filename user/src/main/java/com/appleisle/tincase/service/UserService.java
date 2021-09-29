package com.appleisle.tincase.service;

import com.appleisle.tincase.domain.user.User;
import com.appleisle.tincase.dto.request.JoinForm;
import com.appleisle.tincase.exception.EmailExistsException;
import com.appleisle.tincase.exception.ResourceNotFoundException;
import com.appleisle.tincase.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    @Transactional
    public void join(JoinForm joinForm) {
        if (userRepository.existsByEmail(joinForm.getEmail())) {
            throw new EmailExistsException(joinForm.getEmail());
        }

        User newUser = User.builder()
                .email(joinForm.getEmail())
                .password(passwordEncoder.encode(joinForm.getPassword()))
                .build();

        userRepository.save(newUser);
    }
}
