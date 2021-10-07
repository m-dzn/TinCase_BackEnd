package com.appleisle.tincase.controller;

import com.appleisle.tincase.consts.MappingConsts;
import com.appleisle.tincase.domain.user.UserPrincipal;
import com.appleisle.tincase.dto.request.EditPostForm;
import com.appleisle.tincase.dto.request.NewPostForm;
import com.appleisle.tincase.dto.response.Pagination;
import com.appleisle.tincase.dto.response.PostDetailResponse;
import com.appleisle.tincase.dto.response.PostSummary;
import com.appleisle.tincase.security.CurrentUser;
import com.appleisle.tincase.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping(MappingConsts.POST)
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> writePost(@Valid @RequestBody NewPostForm newPostForm) {
        log.debug("새 포스트 작성");

        Long postId = postService.writePost(newPostForm);

        URI location = UriComponentsBuilder.newInstance()
                .path("/post/" + postId)
                .buildAndExpand().toUri();
        return ResponseEntity.created(location).body(postId);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> readPost(
            @PathVariable @Valid Long postId, @CurrentUser UserPrincipal currentUser)
    {
        Long userId = currentUser != null ? currentUser.getId() : null;
        PostDetailResponse response = postService.readPost(postId, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> editPost(@PathVariable Long id, @Valid @RequestBody EditPostForm editPostForm) {
        return ResponseEntity.ok(postService.editPost(id, editPostForm));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> removePost(@PathVariable Long id) {
        postService.removePost(id);
        return ResponseEntity.ok("포스트가 삭제되었습니다.");
    }

    @GetMapping
    public ResponseEntity<Pagination<PostSummary>> getPostListDesc(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Pagination<PostSummary> response = postService.getPostListDesc(pageNum, pageSize);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{postId}/like")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> likePost(@PathVariable Long postId, @CurrentUser UserPrincipal currentUser) {
        postService.likePost(postId, currentUser.getId());
        return ResponseEntity.ok("좋아요 성공");
    }

    @DeleteMapping("/{postId}/unlike")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> unlikePost(@PathVariable Long postId, @CurrentUser UserPrincipal currentUser) {
        postService.unlikePost(postId, currentUser.getId());
        return ResponseEntity.ok("좋아요 취소 성공");
    }

}
