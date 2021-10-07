package com.appleisle.tincase.domain.post;

import com.appleisle.tincase.domain.BaseEntity;
import com.appleisle.tincase.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User writer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_content_id")
    private PostContent content;

    private Integer viewCnt = 0;

    private Integer replyCnt = 0;

    private Integer likeCnt = 0;

    @Builder
    public Post(String title, User writer, PostContent content) {
        this.title = title;
        this.writer = writer;
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void increaseLikeCnt() {
        this.likeCnt++;
    }

    public void decreaseLikeCnt() {
        if (likeCnt == 0) return;
        this.likeCnt--;
    }
}
