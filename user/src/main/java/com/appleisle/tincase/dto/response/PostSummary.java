package com.appleisle.tincase.dto.response;

import com.appleisle.tincase.domain.post.Post;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSummary {

    private Long id;

    private String title;

    private String writer;

    private LocalDateTime createdAt;

    private Integer viewCnt;

    private Integer replyCnt;

    private Integer likeCnt;

    public PostSummary(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.writer = post.getWriter().getNickname();
        this.createdAt = post.getCreatedAt();
        this.viewCnt = post.getViewCnt();
        this.replyCnt = post.getReplyCnt();
        this.likeCnt = post.getLikeCnt();
    }

    public static PostSummary of(Post post) {
        return new PostSummary(post);
    }

}
