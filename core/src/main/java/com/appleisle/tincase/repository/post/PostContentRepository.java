package com.appleisle.tincase.repository.post;

import com.appleisle.tincase.domain.post.PostContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostContentRepository extends JpaRepository<PostContent, Long> {
}
