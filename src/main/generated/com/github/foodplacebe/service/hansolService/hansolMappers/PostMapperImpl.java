package com.github.foodplacebe.service.hansolService.hansolMappers;

import com.github.foodplacebe.repository.postPhotos.PostPhotos;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.web.dto.hansolDto.PostDetailResponse;
import com.github.foodplacebe.web.dto.hansolDto.PostPhotoDto;
import com.github.foodplacebe.web.dto.hansolDto.PostRegisterRequest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-28T23:21:56+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
public class PostMapperImpl implements PostMapper {

    @Override
    public PostPhotoDto postPhotosToPostPhotoDto(PostPhotos postPhotos) {
        if ( postPhotos == null ) {
            return null;
        }

        PostPhotoDto postPhotoDto = new PostPhotoDto();

        postPhotoDto.setPostId( postPhotosPostsPostId( postPhotos ) );
        postPhotoDto.setPostPhotoId( postPhotos.getPostPhotoId() );
        postPhotoDto.setPhoto( postPhotos.getPhoto() );

        return postPhotoDto;
    }

    @Override
    public PostDetailResponse postsToPostDetailResponse(Posts posts, List<PostPhotoDto> postPhotoDtos, Integer favoriteCount) {
        if ( posts == null && postPhotoDtos == null && favoriteCount == null ) {
            return null;
        }

        PostDetailResponse.PostDetailResponseBuilder postDetailResponse = PostDetailResponse.builder();

        if ( posts != null ) {
            postDetailResponse.userId( postsUserEntityUserId( posts ) );
            postDetailResponse.postId( posts.getPostId() );
            postDetailResponse.name( posts.getName() );
            postDetailResponse.address( posts.getAddress() );
            postDetailResponse.detailAddress( posts.getDetailAddress() );
            postDetailResponse.latitude( posts.getLatitude() );
            postDetailResponse.longitude( posts.getLongitude() );
            postDetailResponse.contactNum( posts.getContactNum() );
            postDetailResponse.category( posts.getCategory() );
            postDetailResponse.menu( posts.getMenu() );
            postDetailResponse.content( posts.getContent() );
            postDetailResponse.viewCount( posts.getViewCount() );
            postDetailResponse.mainPhoto( posts.getMainPhoto() );
            postDetailResponse.createAt( posts.getCreateAt() );
            postDetailResponse.updateAt( posts.getUpdateAt() );
        }
        List<PostPhotoDto> list = postPhotoDtos;
        if ( list != null ) {
            postDetailResponse.postPhotoDtos( new ArrayList<PostPhotoDto>( list ) );
        }
        postDetailResponse.favoriteCount( favoriteCount );

        return postDetailResponse.build();
    }

    @Override
    public Posts postRegisterRequestToPosts(PostRegisterRequest postRegisterRequest, UserEntity userEntity, String neighborhood) {
        if ( postRegisterRequest == null && userEntity == null && neighborhood == null ) {
            return null;
        }

        Posts.PostsBuilder posts = Posts.builder();

        if ( postRegisterRequest != null ) {
            posts.name( postRegisterRequest.getName() );
            posts.address( postRegisterRequest.getAddress() );
            posts.detailAddress( postRegisterRequest.getDetailAddress() );
            posts.latitude( postRegisterRequest.getLatitude() );
            posts.longitude( postRegisterRequest.getLongitude() );
            posts.contactNum( postRegisterRequest.getContactNum() );
            posts.category( postRegisterRequest.getCategory() );
            posts.menu( postRegisterRequest.getMenu() );
            posts.content( postRegisterRequest.getContent() );
            posts.mainPhoto( postRegisterRequest.getMainPhoto() );
        }
        posts.userEntity( userEntity );
        posts.neighborhood( neighborhood );
        posts.createAt( java.time.LocalDateTime.now() );
        posts.viewCount( 0 );

        return posts.build();
    }

    private Integer postPhotosPostsPostId(PostPhotos postPhotos) {
        if ( postPhotos == null ) {
            return null;
        }
        Posts posts = postPhotos.getPosts();
        if ( posts == null ) {
            return null;
        }
        Integer postId = posts.getPostId();
        if ( postId == null ) {
            return null;
        }
        return postId;
    }

    private Integer postsUserEntityUserId(Posts posts) {
        if ( posts == null ) {
            return null;
        }
        UserEntity userEntity = posts.getUserEntity();
        if ( userEntity == null ) {
            return null;
        }
        Integer userId = userEntity.getUserId();
        if ( userId == null ) {
            return null;
        }
        return userId;
    }
}
