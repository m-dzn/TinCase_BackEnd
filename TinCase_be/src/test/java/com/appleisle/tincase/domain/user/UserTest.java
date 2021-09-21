package com.appleisle.tincase.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final String givenEmail = "abc@gmail.com";
    private final String givenPassword = "1234";
    private final List<Role> roles = new ArrayList<>();
    private final Role roleAdmin = new Role(RoleName.ROLE_ADMIN);
    private final Role roleUser = new Role(RoleName.ROLE_USER);

    @BeforeEach
    void init() {
        roles.add(roleAdmin);
        roles.add(roleUser);
    }

    @Test
    void create() {
        User user = User.builder()
                .email(givenEmail)
                .password(givenPassword)
                .build();

        assertAll(
                () -> assertEquals(user.getEmail(), givenEmail),
                () -> assertEquals(user.getPassword(), givenPassword)
        );
    }

    @Test
    void addRole() {
        User user = User.builder()
                .email(givenEmail)
                .password(givenPassword)
                .build();

        roles.forEach(user::addRole);

        assertAll(
                () -> assertTrue(user.getRoles().contains(roleAdmin)),
                () -> assertTrue(user.getRoles().contains(roleUser))
        );
    }

}