package com.github.foodplacebe.repository.posts;

import com.github.foodplacebe.repository.postFavorite.PostFavorite;
import com.github.foodplacebe.repository.users.UserEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "posts")
@Getter
@Setter
@EqualsAndHashCode(of = "postId")
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

    @Column(name = "view_count", nullable = false, columnDefinition = "DEFAULT 0")
    private Integer viewCount;

    @Column(name = "main_photo")
    private String mainPhoto;

    @Column(name = "create_date", nullable = false, columnDefinition = "DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "delete_date")
    private LocalDateTime deleteDate;

    @OneToMany(mappedBy = "posts")
    private Collection<PostFavorite> postFavorites;
}
