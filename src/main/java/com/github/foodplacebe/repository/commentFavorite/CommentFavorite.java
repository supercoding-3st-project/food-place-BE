package com.github.foodplacebe.repository.commentFavorite;

import com.github.foodplacebe.repository.comments.Comments;
import com.github.foodplacebe.repository.users.UserEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comment_favorite")
@Getter
@Setter
@EqualsAndHashCode(of = "CommentFavoriteId")
public class CommentFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_favorite_id")
    private Integer CommentFavoriteId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comments comments;
}
