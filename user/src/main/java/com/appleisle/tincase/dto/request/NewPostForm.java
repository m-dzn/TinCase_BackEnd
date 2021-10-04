package com.appleisle.tincase.dto.request;

import com.appleisle.tincase.domain.post.Post;
import com.appleisle.tincase.domain.post.PostContent;
import com.appleisle.tincase.domain.user.User;
import com.appleisle.tincase.repository.user.UserRepository;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewPostForm {

    @NotBlank
    private String title;

    @NotNull
    private Long writerId;

    @NotBlank
    private String content;

    public Post toEntity(User writer) {
        return Post.builder()
                .title(title)
                .content(new PostContent(content))
                .writer(writer)
                .build();
    }

}
