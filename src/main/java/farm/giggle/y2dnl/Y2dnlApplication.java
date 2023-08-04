package farm.giggle.y2dnl;

import farm.giggle.y2dnl.youtube.Y2Serv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Y2dnlApplication {

    public static void main(String[] args) {
        //todo избавиться полностью от boot
        ApplicationContext context = SpringApplication.run(Y2dnlApplication.class, args);
        // Y2Serv serv = context.getBean(Y2Serv.class);
        // serv.downloadVideo();
    }

}
