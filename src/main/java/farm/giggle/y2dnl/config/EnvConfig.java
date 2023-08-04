package farm.giggle.y2dnl.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;

@Data
@Configuration
@ConfigurationProperties(prefix = "my.server")
public class EnvConfig {
    static private String API_WHAT_DOWNLOAD = "/api/v1/whatdownload";
    static private String DOWNLOADED = "/api/v1/downloaded";
    /**
     * URL сервиса обрабатывающего RSS
     */
    private String y2rssServiceUrl;
    /**
     * Имя файла yt-dlp
     */
    private String loaderFileName;
    /**
     * Абсолютный путь на сервере к файлу yt-dlp
     */
    private String loaderAbsolutePath;
    /**
     * Формат аудио
     */
    private AudioFormat audioFormat;
    /**
     * Абсолютный путь, куда выкладывать файлы
     */
    private String targetAbsolutePath;
    /**
     * URL по которому будут скачиваться файлы
     */
    private String targetUrl;

    public String getWhatDownloadUrl() {
        return y2rssServiceUrl + API_WHAT_DOWNLOAD;
    }

    public String getDownloadedUrl() {
        return y2rssServiceUrl + DOWNLOADED;
    }

    public String getLoaderPath() {
        return (loaderAbsolutePath.trim().isEmpty()) ?
                loaderFileName : loaderAbsolutePath + File.separator + loaderFileName;
    }

    public enum AudioFormat {
        MP3("mp3");

        public String label;

        AudioFormat(String label) {
            this.label = label;
        }
    }
}
