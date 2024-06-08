package farm.giggle.y2dnl.execution;

import farm.giggle.y2dnl.config.SchedulerProperties;
import farm.giggle.y2dnl.config.YtDlpProperties;
import farm.giggle.y2dnl.dto.ExchangeFileFormatDTO;
import farm.giggle.y2dnl.s3services.MinioStorageService;
import farm.giggle.y2dnl.services.DownloadedFile;
import farm.giggle.y2dnl.services.RestApiClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;

@Component
@Slf4j
public class FileDownloader {

    private final RestApiClientService restApiClientService;
    private final MinioStorageService minioStorageService;
    private final ThreadPoolTaskExecutor executor;
    private final SchedulerProperties schedulerProperties;
    private final YtDlpProperties ytDlpProperties;

    public FileDownloader(RestApiClientService restApiClientService, MinioStorageService minioStorageService, @Qualifier("taskExecutor") Executor taskExecutor, SchedulerProperties schedulerProperties, YtDlpProperties ytDlpProperties) {
        this.restApiClientService = restApiClientService;
        this.minioStorageService = minioStorageService;
        this.executor = (ThreadPoolTaskExecutor) taskExecutor;
        this.schedulerProperties = schedulerProperties;
        this.ytDlpProperties = ytDlpProperties;
    }


    @Scheduled(fixedDelay = 5000)
    public void fetchAndProcessFile() {
        if (schedulerProperties.getQueueCapacity() < executor.getQueueSize()) {
            log.info("Queue is full. Skipping fetch until next scheduled run.");
            return;
        }

        ExchangeFileFormatDTO downloadLink = restApiClientService.GetVideoURLRequest();
        if (downloadLink == null) {
            return;
        }
        log.info("Starting processing for file [" + downloadLink.getFileDescriptor() + ", video link " + downloadLink.getVideoUrl() + "]");
        executor.execute(() -> processDownloadLink(downloadLink));
    }

    private void processDownloadLink(ExchangeFileFormatDTO downloadLink) {
        try {
            DownloadedFile downloadedFile = new DownloadedFile(downloadLink, ytDlpProperties);
            if (!downloadedFile.download()) {
                log.error("Failed to download file: " + downloadLink.getFileDescriptor());
                return;
            }
            String s3link = minioStorageService.send(downloadedFile::getAudioFile);
            if (s3link.isBlank()) {
                log.error("Failed to upload file to Minio: " + downloadLink.getFileDescriptor());
                return;
            }

            restApiClientService.sendCompletionResponse(
                    new ExchangeFileFormatDTO(downloadLink, s3link, LocalDateTime.now(Clock.systemUTC())));

            log.info("File has been processed successfully [" + downloadLink.getFileDescriptor() + ", resulting file " + downloadedFile.getAudioFile()+"]");
        } catch (Exception exception) {
            log.error("Error processing link: " + downloadLink.getFileDescriptor(), exception);
        }
    }

}




























