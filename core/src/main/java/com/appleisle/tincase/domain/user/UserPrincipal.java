package com.appleisle.tincase.domain.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class UserPrincipal implements UserDetails, OAuth2User {

    private Long id;

    private String email;

    @Override
    public String getUsername() {
        return email;
    }

    public UserPrincipal(User user, Set<? extends GrantedAuthority> authorities) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorities = authorities;
        this.enabled = this.accountNonExpired = this.accountNonLocked = this.credentialsNonExpired = true;
    }

    private String password;

    private Set<? extends GrantedAuthority> authorities;

    @Setter
    private Map<String, Object> attributes;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    public static UserPrincipal create(User user) {
        Set<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                    new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toSet());

        return new UserPrincipal(user, authorities);
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal principal = UserPrincipal.create(user);
        principal.setAttributes(attributes);

        return principal;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

}
