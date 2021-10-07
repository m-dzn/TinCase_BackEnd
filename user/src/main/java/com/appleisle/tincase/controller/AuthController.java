package com.appleisle.tincase.controller;

import com.appleisle.tincase.consts.MappingConsts;
import com.appleisle.tincase.domain.user.User;
import com.appleisle.tincase.domain.user.UserPrincipal;
import com.appleisle.tincase.dto.request.JoinForm;
import com.appleisle.tincase.dto.response.SuccessResponse;
import com.appleisle.tincase.dto.response.UserSummary;
import com.appleisle.tincase.security.CurrentUser;
import com.appleisle.tincase.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(MappingConsts.AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<SuccessResponse> join(@Valid @RequestBody JoinForm joinForm) {
        userService.join(joinForm);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse("회원 가입 성공"));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserSummary> getCurrentUser(@Valid @CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = UserSummary.of(currentUser.getUser());
        return ResponseEntity.ok(userSummary);
    }

}
