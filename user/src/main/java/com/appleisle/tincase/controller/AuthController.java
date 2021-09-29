package com.appleisle.tincase.controller;

import com.appleisle.tincase.domain.user.User;
import com.appleisle.tincase.dto.request.JoinForm;
import com.appleisle.tincase.dto.request.LoginForm;
import com.appleisle.tincase.dto.response.JwtResponse;
import com.appleisle.tincase.dto.response.SuccessResponse;
import com.appleisle.tincase.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> findUserById(
            @PathVariable Long userId, @RequestParam(defaultValue = "ko") String lang)
    {
        User user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/join")
    public ResponseEntity<SuccessResponse> join(@RequestBody JoinForm joinForm) {
        userService.join(joinForm);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/")
                .buildAndExpand().toUri();

        return ResponseEntity.created(location)
                .body(new SuccessResponse("회원 가입 성공"));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginForm loginForm) {
        log.debug("JWTLoginFilter 통과!");

        return ResponseEntity.ok(new JwtResponse("jwt"));
    }

}
