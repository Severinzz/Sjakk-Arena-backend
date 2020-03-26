package no.ntnu.sjakkarena.config;

import no.ntnu.sjakkarena.subscriberhandler.PlayerSubscriberHandler;
import no.ntnu.sjakkarena.subscriberhandler.TournamentSubscriberHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
public class AppConfig {

    @Bean
    public TournamentSubscriberHandler tournamentSubscriberHandler(){
        return new TournamentSubscriberHandler();
    }

    @Bean
    public PlayerSubscriberHandler playerSubscriberHandler(){
        return new PlayerSubscriberHandler();
    }
}
