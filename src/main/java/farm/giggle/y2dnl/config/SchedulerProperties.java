package farm.giggle.y2dnl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "scheduler")
public class SchedulerProperties {
    /**
     * Размер пула потоков - минимальное значение
     */
    private int corePoolSize;
    /**
     * Размер пула потоков - максимальное значение
     */
    private int maxPoolSize;
    /**
     * Размер очереди задач
     */
    private int queueCapacity;
    /**
     * Запланированная задержка между опросами API
     */
    private Long scheduledDelayMs;
}
