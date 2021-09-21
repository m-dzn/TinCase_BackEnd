package com.appleisle.tincase.controller;

import com.appleisle.tincase.dto.request.JoinForm;
import com.appleisle.tincase.dto.request.LoginForm;
import com.appleisle.tincase.dto.response.UserSummary;
import com.appleisle.tincase.security.UserPrincipal;
import com.appleisle.tincase.service.inf.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginForm.getEmail(),
                        loginForm.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        // TODO : 로그인 성공 로직 정하기
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        return ResponseEntity.ok(userPrincipal.getUsername());
    }

    @PostMapping("/join")
    public void join(@RequestBody JoinForm joinForm) {
        userService.join(joinForm);
    }

    @GetMapping
    public ResponseEntity<List<UserSummary>> getUserList() {
        List<UserSummary> response = userService.getUserList();
        return ResponseEntity.ok(response);
    }

}
