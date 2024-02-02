package com.github.foodplacebe.service.authAccount.test;

import com.github.foodplacebe.repository.comments.Comments;
import com.github.foodplacebe.repository.comments.CommentsJpa;
import com.github.foodplacebe.repository.postFavorite.PostFavorite;
import com.github.foodplacebe.repository.postFavorite.PostFavoriteJpa;
import com.github.foodplacebe.repository.posts.Posts;
import com.github.foodplacebe.repository.posts.PostsJpa;
import com.github.foodplacebe.repository.userDetails.CustomUserDetails;
import com.github.foodplacebe.repository.users.UserEntity;
import com.github.foodplacebe.repository.users.UserJpa;
import com.github.foodplacebe.service.exceptions.BadRequestException;
import com.github.foodplacebe.service.exceptions.NotFoundException;
import com.github.foodplacebe.service.hansolService.hansolMappers.PostMapper;
import com.github.foodplacebe.web.controller.authAccount.test.TestDto;
import com.github.foodplacebe.web.dto.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TestService {
    private final PostsJpa postsJpa;
    private final UserJpa userJpa;
    private final PostFavoriteJpa postFavoriteJpa;
    private final CommentsJpa commentsJpa;

    //조아여시러여
    @Transactional(transactionManager = "tm")
    public ResponseDto lkDislk(Integer postId, CustomUserDetails customUserDetails) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()->new NotFoundException("로그인 정보 없음", customUserDetails.getUserId()));
        Posts posts = postsJpa.findById(postId)
                .orElseThrow(()->new NotFoundException("게시글 정보 없음", postId));

        List<UserEntity> pushLikeUser = posts.getPostFavorites().stream()
                .map(pf->pf.getUserEntity()).toList();

        if(!pushLikeUser.contains(userEntity)){
            PostFavorite postFavorite = PostFavorite.builder()
                    .userEntity(userEntity).posts(posts)
                    .build();
            postFavoriteJpa.save(postFavorite);
            Map<String, Object> titleAndLikeCount = new HashMap<>();
            titleAndLikeCount.put("게시글 제목", posts.getName());
            titleAndLikeCount.put("좋아요 갯수", posts.getPostFavorites().size()+1);
            return new ResponseDto(HttpStatus.OK.value(), "좋아요를 눌렀습니다.", titleAndLikeCount);
        }else{
            postFavoriteJpa.deleteByUserEntityAndPosts(userEntity,posts);
            postFavoriteJpa.findByUserEntityAndPosts(userEntity,posts);
            Map<String, Object> titleAndLikeCount = new HashMap<>();
            titleAndLikeCount.put("게시글 제목", posts.getName());
            titleAndLikeCount.put("좋아요 갯수", posts.getPostFavorites().size()-1);
            return new ResponseDto(HttpStatus.OK.value(), "좋아요를 취소했습니다.", titleAndLikeCount);
        }
    }


    //댓글조회 대댓글조회구현
    public ResponseDto getComments(Integer postId) {
        Posts posts = postsJpa.findById(postId)
                .orElseThrow(()->new NotFoundException("게시물 정보 없음", postId));
        List<Comments> comments = posts.getComments();

        List<Map<String,Object>> responseMap = new ArrayList<>();

        for (Comments c : comments) {
            if (c.getParentCommentId() == 0) {
                //응답 만들기
                Map<String, Object> response = createMapResponse(c);

                //자식찾기
                List<Map<String, Object>> childComments = findChildComments(comments, c.getCommentId());

                if (!childComments.isEmpty()) {
                    response.put("대댓글", childComments);
                }

                responseMap.add(response);
            }
        }
        return new ResponseDto(HttpStatus.OK.value(), posts.getName()+"의 댓글 조회 성공", responseMap);
    }
    private List<Map<String, Object>> findChildComments(List<Comments> comments, Integer parentCommentId) {
        List<Map<String, Object>> childComments = new ArrayList<>();

        for (Comments c : comments) {
            if (c.getParentCommentId().equals(parentCommentId)) {
                //기본 응답 만들기
                Map<String, Object> response = createMapResponse(c);
                //얘네는 부모가 있으니 부모닉넴도 넣기
                response.put("부모닉넴", c.getParentComment().getUserEntity().getNickName());

                // 손주찾기 (자기자신도 자식이 있을 수 있으니 동일메서드 들어갔다나옴)
                List<Map<String, Object>> grandchildComments = findChildComments(comments, c.getCommentId());
                if (!grandchildComments.isEmpty()) {
                    response.put("대댓글", grandchildComments);
                }

                childComments.add(response);
            }
        }
        return childComments;
    }


    private Map<String, Object> createMapResponse(Comments c){
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("댓글아이디", c.getCommentId());
        response.put("작성자", c.getUserEntity().getNickName());
        response.put("댓글내용", c.getContent());
        response.put("좋아요갯수", c.getCommentFavorites().size());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 - HH시 mm분");
        response.put("작성일", c.getCreateAt().format(dateTimeFormatter));

        return response;
    }

    //    private Map<String, Object> createCommentResponse(Comments c, List<Comments> allComments) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("댓글아이디", c.getCommentId());
//        response.put("작성자", c.getUserEntity().getNickName());
//        response.put("댓글내용", c.getContent());
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 - HH시 mm분");
//        response.put("작성일", c.getCreateAt().format(dateTimeFormatter));
//        response.put("좋아요갯수", c.getCommentFavorites().size());
//
//
//        List<Map<String, Object>> childComments = findChildComments(allComments, c.getCommentId());
//        if (!childComments.isEmpty()) {
//            response.put("대댓글", childComments);
//        }
//
//        return response;
//    }


    //댓글등록
    @Transactional (transactionManager = "tm")
    public ResponseDto createComment(Integer postId, Integer commentId, CustomUserDetails customUserDetails, TestDto testDto) {
        UserEntity userEntity = userJpa.findById(customUserDetails.getUserId())
                .orElseThrow(()->new NotFoundException("유저 정보 없음", customUserDetails.getUserId()));
        Posts posts = postsJpa.findById(postId)
                .orElseThrow(()->new NotFoundException("게시물 정보 없음", postId));
        Comments comments = new Comments();

        if(commentId!=null){
            comments.setPosts(posts);
            comments.setUserEntity(userEntity);
            comments.setContent(testDto.getContent());
            comments.setParentCommentId(commentId);
            comments.setCreateAt(LocalDateTime.now());
            comments.setDeleteStatus(false);
        }else{

            comments.setPosts(posts);
            comments.setUserEntity(userEntity);
            comments.setContent(testDto.getContent());
            comments.setParentCommentId(0);
            comments.setCreateAt(LocalDateTime.now());
            comments.setDeleteStatus(false);
        }
        commentsJpa.save(comments);
        Map<String,Object> response = new HashMap<>();
        response.put("댓글아디", comments.getCommentId());
        response.put("작성자", userEntity.getNickName());
        DateTimeFormatter dateTimeFormatter =DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 - HH시 mm분");
        response.put("작성일", comments.getCreateAt().format(dateTimeFormatter));
        response.put("내용", comments.getContent());
        return new ResponseDto(HttpStatus.OK.value(), "댓글 등록 성공", response);
    }


    public ResponseDto getAllPosts(Sort sort) {
        List<Posts> postsList = postsJpa.findAll(Sort.by(Sort.Direction.DESC,"createAt"));
        List<TestDto> testDtos = postsList.stream().map(p->PostMapper.INSTANCE.postsToTestDto(p)).toList();
        return new ResponseDto(HttpStatus.OK.value(), " sort = "+sort+", 모든 게시글 조회 완료", testDtos);
    }

    public ResponseDto getPageablePosts(int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        try {
            Page<Posts> postsPage = postsJpa.findAll(pageable);
            Page<TestDto> testDtoPage = postsPage.map(p->PostMapper.INSTANCE.postsToTestDto(p));
            return new ResponseDto(HttpStatus.OK.value(), "page = "+pageable.getPageNumber()+" size = "+pageable.getPageSize()+" sort = "+pageable.getSort()+", 조회 완료", testDtoPage);
        }catch (PropertyReferenceException propertyReferenceException){
            throw new BadRequestException("잘못된 정렬 요청",sort.toString());
        }
    }
}
