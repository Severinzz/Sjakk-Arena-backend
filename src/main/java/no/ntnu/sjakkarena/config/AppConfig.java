package no.ntnu.sjakkarena.config;

import no.ntnu.sjakkarena.subscriberhandler.PlayerSubscriberHandler;
import no.ntnu.sjakkarena.subscriberhandler.TournamentSubscriberHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public TournamentSubscriberHandler tournamentDBChangeNotifier(){
        return new TournamentSubscriberHandler();
    }

    @Bean
    public PlayerSubscriberHandler playerDBChangeNotifier(){
        return new PlayerSubscriberHandler();
    }
}
