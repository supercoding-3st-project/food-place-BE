package com.github.foodplacebe.service.PostsService;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.github.foodplacebe.service.exceptions.FileUploadFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableCaching
public class PostPhotosService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String buildFileName(String name, String originFileName, int sequence){

        String fileExtension = FilenameUtils.getExtension(originFileName);
        String fileName = "photo" + "/" + name + "/" + name;

        if (!fileExtension.isEmpty()) {
            fileName += "_" + sequence + "." + fileExtension;
        } else {
            fileName += "_" + sequence;
        }

        return fileName;
    }

    public String uploadProfileImg (String name, MultipartFile multipartFile) {
        String uploadedUrl;
        if (multipartFile != null && !multipartFile.isEmpty()){
            try {
                String fileName = buildFileName(name, multipartFile.getOriginalFilename(), 1);
                uploadedUrl = uploadImage(multipartFile, fileName);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("img upload failed");
            }
        }else {
            uploadedUrl = "http://k.kakaocdn.net/dn/1G9kp/btsAot8liOn/8CWudi3uy07rvFNUkk3ER0/img_640x640.jpg";
        }
        return uploadedUrl;
    }

    public String uploadImage(MultipartFile multipartFile, String fileName) throws FileUploadFailedException{
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentDisposition("inline");

        try(InputStream inputStream = multipartFile.getInputStream()){
            amazonS3Client.putObject(new EncryptedPutObjectRequest(bucketName, fileName, inputStream,objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        }catch (Exception e){
            log.error("Amazon S3 파일 업로드 실패: {}", e.getMessage(), e);
            throw new FileUploadFailedException("파일 업로드에 실패했습니다");
        }
        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    // 이미지 여러개 업로드
    public List<String> uploadImage(Integer postId, List<MultipartFile> multipartFiles) {
        List<String> fileNameList = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            String fileName = createFileName(postId, file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
            }

            fileNameList.add(amazonS3Client.getUrl(bucketName, fileName).toString());
        }

        return fileNameList;
    }

    // 포스트 아이디 폴더 안에 이미지 이름 생성
    private String createFileName(Integer postId, String originFileName) {

        String fileExtension = FilenameUtils.getExtension(originFileName);
        String fileName = "photo" + "/" + postId + "/" + UUID.randomUUID();

        if (!fileExtension.isEmpty()) {
            fileName += "_" + fileExtension;
        } else {
            fileName += "_" ;
        }

        return fileName;
    }

    // 이미지 수정으로 인해 기존 이미지 삭제 메소드
    public void deleteImage(String fileUrl) {
        String splitStr = ".com/";
        String fileName = fileUrl.substring(fileUrl.lastIndexOf(splitStr) + splitStr.length());

        amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }

    // 이미지 단일 업로드
    public String uploadImageV1(Integer postId, MultipartFile file) {
        String fileName = createFileName(postId, file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try(InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }

        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }


}
