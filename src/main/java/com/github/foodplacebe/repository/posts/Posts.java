package com.github.foodplacebe.repository.posts;

import com.github.foodplacebe.repository.comments.Comments;
import com.github.foodplacebe.repository.postFavorite.PostFavorite;
import com.github.foodplacebe.repository.postPhotos.PostPhotos;
import com.github.foodplacebe.repository.users.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@EqualsAndHashCode(of = "postId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "neighborhood", nullable = false)
    private String neighborhood;

    @Column(name = "latitude", nullable = false)
    private String latitude;

    @Column(name = "longitude", nullable = false)
    private String longitude;

    @Column(name = "contact_num", nullable = false)
    private String contactNum;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "menu", nullable = false)
    private String menu;

    @Column(name = "content")
    private String content;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "main_photo")
    private String mainPhoto;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "delete_at")
    private LocalDateTime deleteAt;

    @OneToMany(mappedBy = "posts")
    private Collection<PostFavorite> postFavorites;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostPhotos> postPhotos = new ArrayList<>();

    @OneToMany(mappedBy = "posts")
    private List<Comments> comments;
}
