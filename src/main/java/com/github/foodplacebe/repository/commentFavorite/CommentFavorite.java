package com.github.foodplacebe.repository.commentFavorite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.foodplacebe.repository.comments.Comments;
import com.github.foodplacebe.repository.users.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment_favorite")
@Getter
@Setter
@EqualsAndHashCode(of = "CommentFavoriteId")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_favorite_id")
    private Integer CommentFavoriteId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;


    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comments comments;
}
