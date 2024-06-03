package farm.giggle.y2dnl.services;

import farm.giggle.y2dnl.config.YtDlpProperties;
import farm.giggle.y2dnl.dto.ExchangeFileFormatDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Data
@Slf4j
public class DownloadedFile {
    final String videoURl;
    final YtDlpProperties envConfig;
    final UUID fileUUID = UUID.randomUUID();
    private String fileNameAudio;
    private String fileNameAudioFull;
    private String fileNameOutput;

    public DownloadedFile(ExchangeFileFormatDTO fileDTO, YtDlpProperties ytDlpProperties) {
        this.videoURl = fileDTO.getVideoUrl();
        this.envConfig = ytDlpProperties;
    }

    public boolean download() {
        fileNameAudio = fileUUID + "." + envConfig.getAudioFormat().label;
        fileNameAudioFull = envConfig.getTargetAbsolutePath() + File.separator + fileNameAudio;;
        fileNameOutput = envConfig.getTargetAbsolutePath() + File.separator + fileUUID + ".txt";
        String paramAudioFormat = "--audio-format=" + envConfig.getAudioFormat().label;
        String fileUrl = envConfig.getTargetUrl() + "/" + fileNameAudio;

        log.debug("download()");
        log.debug("getLoaderPath() =" + envConfig.getLoaderPath());
        log.debug("getTargetAbsolutePath() =" + envConfig.getTargetAbsolutePath());
        log.debug("getLoaderAbsolutePath() =" + envConfig.getLoaderAbsolutePath());
        log.debug("fileDownload: " + fileNameAudio + ", process output: " + fileNameOutput + ", format: " + paramAudioFormat);

        try {
            if (call_YTDLP_forDownload(fileNameAudio, fileNameOutput, paramAudioFormat, videoURl) == 0) {
                if (Files.exists(Path.of(fileNameAudioFull))) {
                    return true;
//                    java.nio.file.attribute.FileTime fileTime = Files.getLastModifiedTime(downloadedFilePath);
//                    LocalDateTime localDateTime = LocalDateTime.ofInstant(fileTime.toInstant(), ZoneOffset.UTC);
//                    downloadedFiles.add(new ExchangeFileFormatDTO(file.getVideoUrl(), fileUrl, localDateTime));
                }
            }
        } catch (Exception e) {
            log.error("Download process fault {},{}", videoURl, e.getMessage(), e);
        }
        return false;
    }

    private int call_YTDLP_forDownload(String fileNameAudio, String fileNameOutput, String paramAudioFormat, String videoUrl) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                envConfig.getLoaderPath(),
                "-P " + envConfig.getTargetAbsolutePath(),
                "-o" + fileNameAudio,
                "--extract-audio", paramAudioFormat,
                videoUrl);
//        ProcessBuilder pb = new ProcessBuilder(
//                envConfig.getLoaderPath(),
//                "-P " + "\"" + envConfig.getTargetAbsolutePath() + "\"",
//                "-o \"" + fileNameAudio + "\"",
//                "--extract-audio", paramAudioFormat,
//                videoUrl);
        pb.redirectOutput(new File(fileNameOutput));
        pb.directory(new File(envConfig.getLoaderAbsolutePath()));
        Process process = pb.start();
        int exitCode = process.waitFor();
        log.debug("Downloaded result " + exitCode);
        //int exitCode = 0;
        log.debug("Downloaded result: " + exitCode + ", file: " + envConfig.getTargetUrl() + File.separator + fileNameAudio);
        return exitCode;
    }

    public Path getAudioFile() {
        return Path.of(envConfig.getTargetAbsolutePath(), fileNameAudio);
    }
}
