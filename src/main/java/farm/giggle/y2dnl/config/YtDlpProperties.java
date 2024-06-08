package farm.giggle.y2dnl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Data
@Configuration
@ConfigurationProperties(prefix = "yt-dlp")
public class YtDlpProperties {
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
