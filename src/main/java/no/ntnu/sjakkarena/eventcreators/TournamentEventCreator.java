package no.ntnu.sjakkarena.eventcreators;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.tournamentevents.TournamentEndedEvent;
import no.ntnu.sjakkarena.events.tournamentevents.TournamentStartedEvent;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Creates tournament Events
 */
@Component
public class TournamentEventCreator {


    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * Creates and publishes an event where a tournament has started
     *
     * @param tournamentId The id of the started tournament
     */
    public void createAndPublishTournamentStartedEvent(int tournamentId){
        List<Player> players = playerRepository.getPlayersInTournament(tournamentId);
        applicationEventPublisher.publishEvent(new TournamentStartedEvent(this, tournamentId, players));
    }

    /**
     * Creates and publishes an event where a tournament has ended
     *
     * @param tournamentId The id of the ended tournament
     */
    public void createAndPublishTournamentEndedEvent(int tournamentId) {
        List<Player> players = playerRepository.getPlayersInTournament(tournamentId);
        applicationEventPublisher.publishEvent((new TournamentEndedEvent(this, tournamentId, players)));
    }


}
