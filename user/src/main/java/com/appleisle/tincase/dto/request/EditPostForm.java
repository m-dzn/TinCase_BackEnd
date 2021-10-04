package com.appleisle.tincase.dto.request;

import com.appleisle.tincase.consts.Constraints;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditPostForm {

    @NotBlank
    @Size(max = Constraints.POST_TITLE_MAX_LENGTH)
    private String title;

    @NotBlank
    private String content;

}
