package com.service.ttucktak.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
public class S3Util {
    private final AmazonS3Client client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3Util(AmazonS3Client client) {
        this.client = client;
    }

    /**
     * AWS S3 업로드 메서드
     * @param file, 업로드 대상 파일
     * @param dirName, (업로드 대상 폴더 ex) profile)
     * */
    public String upload(MultipartFile file, String dirName) throws IOException {
        String filePath = dirName + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        client.putObject(bucket, filePath, file.getInputStream(), metadata);

        return client.getUrl(bucket, filePath).toString();
    }
}
