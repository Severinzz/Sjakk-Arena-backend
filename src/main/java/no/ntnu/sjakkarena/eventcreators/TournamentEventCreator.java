package no.ntnu.sjakkarena.eventcreators;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.tournamentevents.TournamentEndedEvent;
import no.ntnu.sjakkarena.events.tournamentevents.TournamentStartedEvent;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TournamentEventCreator {


    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void createAndPublishTournamentStartedEvent(int tournamentId){
        List<Player> players = playerRepository.getPlayersInTournament(tournamentId);
        applicationEventPublisher.publishEvent(new TournamentStartedEvent(this, tournamentId, players));
    }

    public void createAndPublishTournamentEndedEvent(int tournamentId) {
        List<Player> players = playerRepository.getPlayersInTournament(tournamentId);
        applicationEventPublisher.publishEvent((new TournamentEndedEvent(this, tournamentId, players)));
    }


}
