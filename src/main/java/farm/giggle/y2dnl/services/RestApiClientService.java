package farm.giggle.y2dnl.services;

import farm.giggle.y2dnl.config.Y2RssApiProperties;
import farm.giggle.y2dnl.config.YtDlpProperties;
import farm.giggle.y2dnl.dto.ExchangeFileFormatDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class RestApiClientService {

    private final RestTemplate restTemplate;
    private final Y2RssApiProperties y2RssApiProperties;


    public RestApiClientService(@Qualifier("getBeanRestTemplate") RestTemplate restTemplate, YtDlpProperties envConfig, Y2RssApiProperties y2RssApiProperties) {
        this.restTemplate = restTemplate;
        this.y2RssApiProperties = y2RssApiProperties;
    }

    public ExchangeFileFormatDTO GetVideoURLRequest() {
        try {
            ResponseEntity<ExchangeFileFormatDTO> response =
                    restTemplate.getForEntity(y2RssApiProperties.getURLMethodGetFileToDownload(), ExchangeFileFormatDTO.class);
            if (HttpStatus.OK.equals(response.getStatusCode())) {
                return response.getBody();
            }

        } catch (RestClientException ex) {
            log.error("Failed to establish API connection", ex);
        }
        return null;
    }

    public void sendCompletionResponse(ExchangeFileFormatDTO exchangeFileFormatDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ExchangeFileFormatDTO> httpEntity = new HttpEntity<>(exchangeFileFormatDTO, headers);
        restTemplate.exchange(y2RssApiProperties.getURLMethodPutDownloadedFile(), HttpMethod.PUT, httpEntity, String.class);
    }
}
