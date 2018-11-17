package szczkrzy.kanteam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class KanTeamBackendApplication {

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("CET"));
    }

    public static void main(String[] args) {
        SpringApplication.run(KanTeamBackendApplication.class, args);
    }
}
