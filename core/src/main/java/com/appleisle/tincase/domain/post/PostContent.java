package com.appleisle.tincase.domain.post;

import com.appleisle.tincase.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_content_id")
    private Long id;

    
    private String content;

    public PostContent(String content) {
        this.content = content;
    }

    public void edit(String content) {
        this.content = content;
    }

}
