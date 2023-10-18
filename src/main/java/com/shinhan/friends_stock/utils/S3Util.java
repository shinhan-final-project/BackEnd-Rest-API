package com.shinhan.friends_stock.utils;

import com.shinhan.friends_stock.exception.ResourceNotPublishedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;

@Component
@RequiredArgsConstructor
public class S3Util {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    public void upload(String key, String content) {

        try {
            byte[] jsonData = content.getBytes();

            InputStream inputStream = new ByteArrayInputStream(jsonData);
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, jsonData.length));
        } catch (S3Exception e) {
            e.printStackTrace();
        }
    }

    public String download(String key) {

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(s3Object));

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            return content.toString();
        } catch (S3Exception | IOException e) {
            throw new ResourceNotPublishedException("주가 정보를 찾을 수 없습니다.");
        }
    }

}
