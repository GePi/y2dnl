package farm.giggle.y2dnl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Y2dnlApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Y2dnlApplication.class, args);
        // Y2Serv serv = context.getBean(Y2Serv.class);
        // serv.downloadVideo();
    }
}
