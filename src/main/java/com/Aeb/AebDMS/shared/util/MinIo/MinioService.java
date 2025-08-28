package com.Aeb.AebDMS.shared.util.MinIo;

import com.Aeb.AebDMS.app.advicer.exceptions.BaseException;
import io.minio.*;
import io.minio.http.Method;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class MinioService {

    private final MinioClient minioClient;
    private final MinioProperties properties;

    public MinioService(MinioProperties properties) {
        this.properties = properties;
        this.minioClient = MinioClient.builder()
                .endpoint(properties.getUrl())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();

        try {
            // Create bucket if it doesn't exist
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(properties.getBucket())
                    .build());

            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(properties.getBucket())
                        .build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize MinIO bucket", e);
        }
    }

    public String upload(MultipartFile file, String ext) {
        try (InputStream inputStream = file.getInputStream()) {
            UUID id = UUID.randomUUID();
            String objectName = id + "." + ext;
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            return objectName;
        } catch (Exception e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    public InputStream getObjectStream(String objectName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file from MinIO", e);
        }
    }

    public String getObjectUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket("dms-files")
                            .object(objectName)
                            .expiry(60 * 5) // 10 minutes
                            .build()
            );

        } catch (Exception e) {
            throw new BaseException("Failed to read file from MinIO", HttpStatus.NOT_FOUND);
        }
    }

    public void delete(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from MinIO", e);
        }
    }
}

