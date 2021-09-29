package com.appleisle.tincase.service;

import com.appleisle.tincase.domain.user.User;
import com.appleisle.tincase.domain.user.UserRepository;
import com.appleisle.tincase.dto.request.JoinForm;
import com.appleisle.tincase.dto.response.UserSummary;
import com.appleisle.tincase.service.inf.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void join(JoinForm joinForm) {
        String encodedPassword = passwordEncoder.encode(joinForm.getPassword());

        User user = User.builder()
                .email(joinForm.getEmail())
                .password(encodedPassword)
                .build();

        userRepository.save(user);
    }

    @Override
    public List<UserSummary> getUserList() {
        return userRepository.findAll()
                .stream().map(UserSummary::of).collect(Collectors.toList());
    }

}
