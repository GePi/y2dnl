package farm.giggle.y2dnl.services;

import farm.giggle.y2dnl.dto.ExchangeFileFormatDTO;
import farm.giggle.y2dnl.config.YtDlpProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
@Slf4j
public class FileDownloadService {

    final YtDlpProperties ytDlpProperties;
    private final RestApiClientService restApiClientService;
    private final MinioStorageService minioStorageService;


    public FileDownloadService(YtDlpProperties ytDlpProperties, RestApiClientService restApiClientService, MinioStorageService minioStorageService) {
        this.ytDlpProperties = ytDlpProperties;
        this.restApiClientService = restApiClientService;
        this.minioStorageService = minioStorageService;
    }

    @Scheduled(fixedDelay = 5000)
    public void fetchAndProcessFile() {
        ExchangeFileFormatDTO downloadLink = restApiClientService.GetVideoURLRequest();
        if (downloadLink == null) {
            return;
        }

        processVideoLink(downloadLink);
    }

    private void processVideoLink(ExchangeFileFormatDTO downloadLink) {

        DownloadedFile downloadedFile = new DownloadedFile(downloadLink, ytDlpProperties);
        if (!downloadedFile.download()) {
            return;
        }
        String s3link = minioStorageService.send(downloadedFile::getAudioFile);
        if (s3link.isBlank()) {
            return;
        }

//        restApiClientService.sendCompletionResponse(
//                new ExchangeFileFormatDTO(downloadLink, s3link, LocalDateTime.now(Clock.systemUTC())));
    }
}




























