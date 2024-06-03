package farm.giggle.y2dnl.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
@EnableScheduling
@Slf4j
public class ApplicationConfig {
    private final Y2RssApiProperties y2RssApiProperties;
    private final MinioProperties minioProperties;

    public ApplicationConfig(Y2RssApiProperties y2RssApiProperties, MinioProperties minioProperties) {
        this.y2RssApiProperties = y2RssApiProperties;
        this.minioProperties = minioProperties;
    }

    @Bean
    public RestTemplate getBeanRestTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder
                .basicAuthentication(y2RssApiProperties.getUsername(), y2RssApiProperties.getPassword())
                .build();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    @Bean
    public MinioClient getBeanMinioClient() {
        MinioClient minioClient = null;
        try {
            minioClient =
                    MinioClient.builder()
                            .endpoint(minioProperties.getEndpoint())
                            .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                            .build();
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
                log.info(String.format("Bucket %s has bean created.", minioProperties.getBucketName()));
            }
        } catch (MinioException e) {
            log.error("MinIO error occurred: " + e);
            log.error("MinIO HTTP trace: " + e.httpTrace());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("MinIO client creation error: " + e);
            throw new RuntimeException(e);
        }
        return minioClient;
    }
}
