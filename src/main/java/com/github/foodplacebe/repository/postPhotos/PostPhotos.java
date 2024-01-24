package com.github.foodplacebe.repository.postPhotos;

import com.github.foodplacebe.repository.posts.Posts;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_photos")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "postPhotoId")
public class PostPhotos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_photo_id")
    private Integer postPhotoId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Posts posts;

    @Column(name = "photo", nullable = false)
    private String photo;
}
