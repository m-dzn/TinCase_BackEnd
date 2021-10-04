package com.appleisle.tincase.service;

import com.appleisle.tincase.domain.post.Post;
import com.appleisle.tincase.domain.user.User;
import com.appleisle.tincase.dto.request.EditPostForm;
import com.appleisle.tincase.dto.request.NewPostForm;
import com.appleisle.tincase.dto.response.Pagination;
import com.appleisle.tincase.dto.response.PostDetailResponse;
import com.appleisle.tincase.dto.response.PostSummary;
import com.appleisle.tincase.exception.ResourceNotFoundException;
import com.appleisle.tincase.repository.post.PostRepository;
import com.appleisle.tincase.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long writePost(NewPostForm newPostForm) {
        User writer = userRepository.findById(newPostForm.getWriterId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", newPostForm.getWriterId()));

        Post newPost = newPostForm.toEntity(writer);

        return postRepository.save(newPost).getId();
    }

    @Transactional(readOnly = true)
    public PostDetailResponse readPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        return PostDetailResponse.of(post);
    }

    @Transactional
    public Long editPost(Long id, EditPostForm editPostForm) {
        return postRepository.findById(id)
                .map(post -> {
                    post.setTitle(editPostForm.getTitle());
                    post.getContent().edit(editPostForm.getContent());

                    return post.getId();
                }).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    }

    @Transactional
    public void removePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public Pagination<PostSummary> getPostListDesc(Integer pageNum, Integer pageSize) {
        PageRequest pageable = PageRequest.of(pageNum, pageSize, Sort.Direction.DESC, "createdAt");
        Page<PostSummary> postPages = postRepository.findAll(pageable)
                .map(PostSummary::of);

        return Pagination.of(postPages);
    }

}
