package com.example.bidly.global.service;

import com.example.bidly.global.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.example.bidly.global.exception.ErrorCode.S3_UPLOAD_FAILED;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file, String folder, Long id) {
        String fileName = folder + "/" + id + "/" + UUID.randomUUID() + getExtension(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (IOException ioe) {
            throw new ServerException(S3_UPLOAD_FAILED);
        }

        return getUrl(fileName);
    }

    // 이미지 개별 삭제
    public void delete(String imageUrl) {
        String key = imageUrl.substring(imageUrl.indexOf(".com/") + 5);
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build());
    }

    // 폴더 내 전체 이미지 삭제
    public void deleteFolder(String folder, String id) {
        String prefix = folder + "/" + id + "/";

        ListObjectsV2Response response = s3Client.listObjectsV2(
                ListObjectsV2Request.builder()
                        .bucket(bucket)
                        .prefix(prefix)
                        .build());

        List<ObjectIdentifier> objects = response.contents().stream()
                .map(o -> ObjectIdentifier.builder()
                        .key(o.key())
                        .build()
                ).toList();

        if (!objects.isEmpty()) {
            s3Client.deleteObjects(DeleteObjectsRequest.builder()
                    .bucket(bucket)
                    .delete(Delete.builder().objects(objects).build())
                    .build());
        }
    }

    private String getUrl(String fileName) {
        return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }
}
