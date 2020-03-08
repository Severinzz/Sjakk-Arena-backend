package no.ntnu.sjakkarena.config;

import no.ntnu.sjakkarena.DBChangeNotifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public DBChangeNotifier dbChangeNotifier(){
        return new DBChangeNotifier();
    }
}
