package farm.giggle.y2dnl.s3services;

import farm.giggle.y2dnl.config.MinioProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest()
class MinioStorageServiceTest {
    @Autowired
    MinioStorageService minioStorageService;
    @Autowired
    MinioProperties minioProperties;

    @Test
    void sendFile() {
        String objectName = "test.txt";
        String url = minioStorageService.send(() -> Path.of(objectName));
        String requiredUrl = minioProperties.getEndpoint() + "/" + minioProperties.getBucketName() + "/" + objectName;
        assertEquals(requiredUrl, url);
        minioStorageService.delete(objectName);
    }
}