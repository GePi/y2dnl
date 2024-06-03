package farm.giggle.y2dnl.services;

import farm.giggle.y2dnl.config.MinioProperties;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.function.Supplier;

@Service
@Slf4j
public class MinioStorageService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public MinioStorageService(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    public String send(Supplier<Path> pathSupplier) {
        Path path = pathSupplier.get();
        String objectName = path.getFileName().toString();
        String bucketName = minioProperties.getBucketName();
        String url = null;
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(Files.newInputStream(path), path.toFile().length(), -1)
                            .contentType("application/octet-stream")
                            .build());

            url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .build());

            url = StringUtils.substringBefore(url, "?");

        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("S3 Minio file send error : " + e);
            throw new RuntimeException(e);
        }
        return url;
    }
}
