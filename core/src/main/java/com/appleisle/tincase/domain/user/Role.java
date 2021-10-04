package com.appleisle.tincase.domain.user;

import com.appleisle.tincase.enumclass.RoleName;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {

    @Id
    @Column(name = "role_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RoleName name;

    @Builder
    public Role(RoleName name) {
        this.id = name.getId();
        this.name = name;
    }

}
