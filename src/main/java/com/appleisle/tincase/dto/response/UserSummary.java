package com.appleisle.tincase.dto.response;

import com.appleisle.tincase.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserSummary {

    private Long id;

    private String email;

    private String nickname;

    private String avatar;

    public static UserSummary of(User user) {
        UserSummary dto = new UserSummary();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setAvatar(user.getAvatar());

        return dto;
    }
}
