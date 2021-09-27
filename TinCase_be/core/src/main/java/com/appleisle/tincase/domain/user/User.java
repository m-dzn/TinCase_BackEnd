package com.appleisle.tincase.domain.user;

import com.appleisle.tincase.domain.BaseEntity;
import com.appleisle.tincase.enumclass.RoleName;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String nickname;

    private String avatar;

    private String address;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Builder
    public User(String email, String password) {
        Assert.hasText(email, "email must not empty");
        Assert.hasText(password, "password must not empty");

        this.email = email;
        this.password = password;
        addRole(RoleName.ROLE_USER);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void addRole(RoleName name) {
        Role role = Role.builder().name(name).build();
        roles.add(role);
    }

}
