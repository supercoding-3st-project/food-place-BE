package com.github.foodplacebe.repository.postFavorite;

import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.users.UserEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_favorite")
@Getter
@Setter
@EqualsAndHashCode(of = "postFavoriteId")
public class PostFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_favorite_id")
    private Integer postFavoriteId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Posts posts;
}
