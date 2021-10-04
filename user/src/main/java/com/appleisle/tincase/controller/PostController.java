package com.appleisle.tincase.controller;

import com.appleisle.tincase.consts.MappingConsts;
import com.appleisle.tincase.dto.request.EditPostForm;
import com.appleisle.tincase.dto.request.NewPostForm;
import com.appleisle.tincase.dto.response.Pagination;
import com.appleisle.tincase.dto.response.PostDetailResponse;
import com.appleisle.tincase.dto.response.PostSummary;
import com.appleisle.tincase.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = MappingConsts.POST)
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Long> writePost(@Valid @RequestBody NewPostForm newPostForm) {
        Long postId = postService.writePost(newPostForm);

        return ResponseEntity.status(HttpStatus.CREATED).body(postId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDetailResponse> readPost(@PathVariable @Valid Long id) {
        PostDetailResponse response = postService.readPost(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> editPost(@PathVariable Long id, @Valid @RequestBody EditPostForm editPostForm) {
        return ResponseEntity.ok(postService.editPost(id, editPostForm));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removePost(@PathVariable Long id) {
        postService.removePost(id);
        return ResponseEntity.ok("포스트가 삭제되었습니다.");
    }

    @GetMapping
    public ResponseEntity<Pagination<PostSummary>> getPostListDesc(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {
        Pagination<PostSummary> response = postService.getPostListDesc(pageNum, pageSize);

        return ResponseEntity.ok(response);
    }

}
