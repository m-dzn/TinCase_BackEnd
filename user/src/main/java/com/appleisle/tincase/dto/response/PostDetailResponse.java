package com.appleisle.tincase.dto.response;

import com.appleisle.tincase.domain.post.Post;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDetailResponse {

    private Long id;

    private String title;

    private String writer;

    private Long writerId;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer viewCnt;

    private Integer replyCnt;

    private Integer likeCnt;

    private Boolean like;

    public PostDetailResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.writer = post.getWriter().getNickname();
        this.writerId = post.getWriter().getId();
        this.content = post.getContent().getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.viewCnt = post.getViewCnt();
        this.replyCnt = post.getReplyCnt();
        this.likeCnt = post.getLikeCnt();
    }

    public PostDetailResponse(Post post, boolean like) {
        this(post);
        this.like = like;
    }

    public static PostDetailResponse of(Post post) {
        return new PostDetailResponse(post);
    }

    public static PostDetailResponse of(Post post, boolean like) {
        return new PostDetailResponse(post, like);
    }

}
