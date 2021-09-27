package com.appleisle.tincase.domain.user;

import com.appleisle.tincase.enumclass.RoleName;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
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
