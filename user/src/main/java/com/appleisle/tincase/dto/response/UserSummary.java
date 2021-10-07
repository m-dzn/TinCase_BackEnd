package com.appleisle.tincase.dto.response;

import com.appleisle.tincase.domain.user.Role;
import com.appleisle.tincase.domain.user.User;
import com.appleisle.tincase.domain.user.UserPrincipal;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSummary {

    private Long id;

    private String email;

    private String nickname;

    private String avatar;

    private String address;

    private String addressDetail;

    private Set<Role> roles;

    @Builder
    public UserSummary(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.avatar = user.getAvatar();
        this.address = user.getAddress();
        this.addressDetail = user.getAddressDetail();
        this.roles = user.getRoles();
    }

    public static UserSummary of(User user) {
        return new UserSummary(user);
    }

}
