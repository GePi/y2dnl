package farm.giggle.y2dnl.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;

@Configuration
@EnableScheduling
@Slf4j
public class ApplicationConfig {

    @Bean
    public RestTemplate getBeanRestTemplate(RestTemplateBuilder builder, Y2RssApiProperties y2RssApiProperties) {
        RestTemplate restTemplate = builder
                .basicAuthentication(y2RssApiProperties.getUsername(), y2RssApiProperties.getPassword())
                .build();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    @Bean
    public MinioClient getBeanMinioClient(MinioProperties minioProperties) {
        MinioClient minioClient = null;
        try {
            log.info("minio endpoint = " + minioProperties.getEndpoint());
            log.info("minio_access_key = " + minioProperties.getAccessKey());
            log.info("minio_secure_key = " + minioProperties.getSecretKey());
            minioClient =
                    MinioClient.builder()
                            .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                            .endpoint(minioProperties.getEndpoint(), 443, true)
                            .build();
            if (minioProperties.getTraceOn()) {
                minioClient.traceOn(System.out);
            }
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

    @Bean
    public Executor taskExecutor(SchedulerProperties prop) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(prop.getCorePoolSize());
        executor.setMaxPoolSize(prop.getMaxPoolSize());
        executor.setQueueCapacity(prop.getQueueCapacity());
        executor.setThreadNamePrefix("FileDownloader-");
        executor.initialize();
        return executor;
    }
}
