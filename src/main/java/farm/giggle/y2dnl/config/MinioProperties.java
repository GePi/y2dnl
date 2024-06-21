package farm.giggle.y2dnl.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "minio")
@Configuration
public class MinioProperties {
    String endpoint;
    String secretKey;
    String accessKey;
    String bucketName;
    Boolean traceOn;

    public MinioProperties() {
    }

}
