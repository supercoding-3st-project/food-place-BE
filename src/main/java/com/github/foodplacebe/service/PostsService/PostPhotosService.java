package com.github.foodplacebe.service.PostsService;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.EncryptedPutObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.github.foodplacebe.service.exceptions.FileUploadFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    public List<String> uploadImages(String name, List<MultipartFile> multipartFileList){
        List<String> filenameList = new ArrayList<>();

        if(multipartFileList !=null){

            for (int i = 0; i < multipartFileList.size(); i++) {
                MultipartFile file = multipartFileList.get(i);
                try {
                    String fileName = buildFileName(name, file.getOriginalFilename(), i + 1);
                    String uploadedUrl = uploadImage(file, fileName);
                    filenameList.add(uploadedUrl);
                } catch (FileUploadFailedException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

            }
        }else{
            return null;
        }
        return filenameList;
    }

    public String uploadProfileImg (String name, MultipartFile multipartFile) {
        String uploadedUrl;
        if (!multipartFile.isEmpty()){
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


}
