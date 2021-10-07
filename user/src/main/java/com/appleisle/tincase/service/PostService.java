package com.appleisle.tincase.service;

import com.appleisle.tincase.domain.post.Post;
import com.appleisle.tincase.domain.post.PostLike;
import com.appleisle.tincase.domain.post.PostLikeRepository;
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
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public Long writePost(NewPostForm newPostForm) {
        User writer = userRepository.findById(newPostForm.getWriterId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", newPostForm.getWriterId()));

        Post newPost = newPostForm.toEntity(writer);

        return postRepository.save(newPost).getId();
    }

    @Transactional(readOnly = true)
    public PostDetailResponse readPost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        if (userId == null) {
            return PostDetailResponse.of(post);
        }

        boolean like = postLikeRepository.findByPostIdAndUserId(postId, userId).isPresent();
        return PostDetailResponse.of(post, like);
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
        PageRequest pageable = PageRequest.of(pageNum - 1, pageSize, Sort.Direction.DESC, "createdAt");
        Page<PostSummary> postPages = postRepository.findAll(pageable)
                .map(PostSummary::of);

        return Pagination.of(postPages);
    }

    @Transactional
    public void likePost(Long postId, Long userId) {
        Post post = postRepository.getById(postId);
        PostLike postLike = PostLike.builder()
                .post(post)
                .user(userRepository.getById(userId))
                .build();

        postLikeRepository.save(postLike);
        post.increaseLikeCnt();
    }

    @Transactional
    public void unlikePost(Long postId, Long userId) {
        PostLike postLike = postLikeRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("PostLike", "postId & userId", postId + ", " + userId));

        postLike.getPost().decreaseLikeCnt();
        postLikeRepository.delete(postLike);
    }

}
