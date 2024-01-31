package com.github.foodplacebe.repository.comments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.foodplacebe.repository.commentFavorite.CommentFavorite;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.users.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter
@Setter
@EqualsAndHashCode(of = "commentId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Posts posts;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity userEntity;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "parent_comment_id", nullable = false, columnDefinition = "DEFAULT 0")
    private Integer parentCommentId;

    @Column(name = "delete_status", nullable = false, columnDefinition = "DEFAULT false")
    private Boolean deleteStatus;

    @Column(name = "create_at", nullable = false, columnDefinition = "DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createAt;

    @Column(name = "delete_at")
    private LocalDateTime deleteAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "comments")
    private Collection<CommentFavorite> commentFavorites;



    @ManyToOne
    @JoinColumn(name = "parent_comment_id", insertable = false, updatable = false)
    private Comments parentComment;

    @OneToMany(mappedBy = "parentComment")
    private Collection<Comments> childComments;

}
