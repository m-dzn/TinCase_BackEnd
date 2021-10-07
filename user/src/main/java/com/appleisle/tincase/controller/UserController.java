package com.appleisle.tincase.controller;

import com.appleisle.tincase.consts.MappingConsts;
import com.appleisle.tincase.domain.user.UserPrincipal;
import com.appleisle.tincase.dto.request.UserUpdateForm;
import com.appleisle.tincase.dto.response.UserSummary;
import com.appleisle.tincase.security.CurrentUser;
import com.appleisle.tincase.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(MappingConsts.USER)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping
    public ResponseEntity<UserSummary> updateUser(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody UserUpdateForm request)
    {
        UserSummary userSummary = userService.updateUser(userPrincipal.getId(), request);

        return ResponseEntity.ok(userSummary);
    }

}
