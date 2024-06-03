package farm.giggle.y2dnl.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ExchangeFileFormatDTO {
    @NotNull
    private String videoUrl;
    @NotNull
    private UUID journalID;

    private LocalDateTime processingTime;

    private String downloadedUrl;
    private LocalDateTime downloadedAt;

    public ExchangeFileFormatDTO( @NotNull UUID journalID, @NonNull LocalDateTime processingTime, @NotNull String videoUrl,
                                 String downloadedUrl, LocalDateTime downloadedAt) {
        this.journalID = journalID;
        this.videoUrl = videoUrl;
        this.processingTime = processingTime;
        this.downloadedUrl = downloadedUrl;
        this.downloadedAt = downloadedAt;
    }

    public ExchangeFileFormatDTO(ExchangeFileFormatDTO downloadLink, String downloadedUrl, LocalDateTime downloadedAt) {
        this.journalID = downloadLink.getJournalID();
        this.videoUrl = downloadLink.getVideoUrl();
        this.processingTime = downloadLink.getProcessingTime();
        this.downloadedUrl = downloadedUrl;
        this.downloadedAt = downloadedAt;
    }
}