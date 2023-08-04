package farm.giggle.y2dnl.youtube;

import farm.giggle.y2dnl.api.ExchangeFileFormat;
import farm.giggle.y2dnl.config.EnvConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;

@Component
@Slf4j
public class Y2Serv {
    final RestTemplate restTemplate;
    final EnvConfig envConfig;

    public Y2Serv(RestTemplate restTemplate, EnvConfig envConfig) {
        this.restTemplate = restTemplate;
        this.envConfig = envConfig;
    }

    @Scheduled(fixedDelay = 5000)
    public void downloadVideo() {
        ExchangeFileFormat[] fileArray = requestArrayToDownload();
        download(fileArray);
        sendDownloadedFilesArray(fileArray);
    }

    private ExchangeFileFormat[] requestArrayToDownload() {
        ResponseEntity<ExchangeFileFormat[]> response =
                restTemplate.getForEntity(envConfig.getWhatDownloadUrl(), ExchangeFileFormat[].class);
        return response.getBody();
    }

    private void download(ExchangeFileFormat[] fileArray) {
        log.debug("download");
        for (var file : fileArray) {
            try {
                String fileNameAudio = file.getId() + "." + envConfig.getAudioFormat().label;
                String fileNameOutput = envConfig.getTargetAbsolutePath() + File.separator + file.getId() + ".txt";
                String paramAudioFormat = "--audio-format=" + envConfig.getAudioFormat().label;

                if (fileDownload(fileNameAudio, fileNameOutput, paramAudioFormat, file.getVideoUrl()) == 0) {
                    file.setDownloadedUrl(envConfig.getTargetUrl() + "/" + fileNameAudio);
                }
            } catch (Exception e) {
                log.error("Download process fault { }", e);
                throw new RuntimeException(e);
            }
        }
        log.debug("Download process is finished");
    }

    private void sendDownloadedFilesArray(ExchangeFileFormat[] fileArray) {
        log.debug("sendDownloadedFilesArray");
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ExchangeFileFormat[]> httpEntity = new HttpEntity<>(fileArray, headers);
        ResponseEntity<String> hhh = rt.exchange(envConfig.getDownloadedUrl(), HttpMethod.PUT, httpEntity, String.class);
        log.debug("sendDownloadedFilesArray, status " + hhh.getStatusCode());
    }

    private int fileDownload(String fileNameAudio, String fileNameOutput, String paramAudioFormat, String videoUrl) throws IOException, InterruptedException {
        log.debug("getLoaderPath() =" + envConfig.getLoaderPath());
        log.debug("getTargetAbsolutePath() =" + envConfig.getTargetAbsolutePath());
        log.debug("getLoaderAbsolutePath() =" + envConfig.getLoaderAbsolutePath());
        log.debug("fileDownload: " + fileNameAudio + ", process output: " + fileNameOutput + ", format: " + paramAudioFormat);
        ProcessBuilder pb = new ProcessBuilder(
                envConfig.getLoaderPath(),
                "-P " + envConfig.getTargetAbsolutePath(),
                "-o " + fileNameAudio,
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
}




























