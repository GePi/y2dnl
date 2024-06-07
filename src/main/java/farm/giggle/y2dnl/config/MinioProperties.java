package farm.giggle.y2dnl.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@ConfigurationProperties(prefix = "minio")
@Configuration
public class MinioProperties {
    String endpoint;
    String secretKey;
    String accessKey;
    String bucketName;

    public MinioProperties() {
        System.out.println("MinioProperties.MinioProperties");
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
