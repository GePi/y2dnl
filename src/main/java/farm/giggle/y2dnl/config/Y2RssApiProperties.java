package farm.giggle.y2dnl.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Configuration
@ConfigurationProperties(prefix = "y2rss-api")
public class Y2RssApiProperties {
    private final String methodGetFileToDownload = "/getFileToDownload";
    private final String methodPutDownloadedFile = "/putDownloadedFile";
    /** URL сервиса обрабатывающего RSS */
    private String url;
    /** Имя пользователя basic авторизации сервиса RSS */
    @Getter
    private String username;
    /** Пароль пользователя basic авторизации сервиса RSS */
    @Getter
    private String password;

    public String getURLMethodGetFileToDownload() {
        return url.concat(methodGetFileToDownload);
    }

    public String getURLMethodPutDownloadedFile() {
        return url.concat(methodPutDownloadedFile);
    }
}
