package com.appleisle.tincase.dto.response;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pagination<T> {

    private Integer pageNum;

    private Integer pageSize;

    private Integer totalPages;

    private Long totalElements;

    private List<T> body;

    @Builder
    public Pagination(Integer pageNum, Integer pageSize, Integer totalPages, Long totalElements, List<T> body) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.body = body;
    }

    public static <T> Pagination<T> of(Page<T> pages) {
        return Pagination.<T>builder()
                .pageNum(pages.getNumber() + 1)
                .pageSize(pages.getSize())
                .totalPages(pages.getTotalPages())
                .totalElements(pages.getTotalElements())
                .body(pages.getContent())
                .build();
    }

}
