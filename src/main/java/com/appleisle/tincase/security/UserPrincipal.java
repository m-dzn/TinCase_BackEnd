package com.appleisle.tincase.security;

import com.appleisle.tincase.domain.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserPrincipal implements UserDetails {

    private Long id;

    private String email;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;


    public UserPrincipal(User user, Collection<? extends GrantedAuthority> authorities) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());

        return new UserPrincipal(user, authorities);
    }

    @Override
    public String getUsername() {
        return email;
    }

}
