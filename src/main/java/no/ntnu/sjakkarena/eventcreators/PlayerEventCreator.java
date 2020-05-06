package no.ntnu.sjakkarena.eventcreators;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.playerevents.NewPlayerAddedEvent;
import no.ntnu.sjakkarena.events.playerevents.PlayerListChangeEvent;
import no.ntnu.sjakkarena.events.playerevents.PlayerRemovedEvent;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Creates Player Events.
 */
@Component
public class PlayerEventCreator {

    @Autowired
    protected TournamentRepository tournamentRepository;

    @Autowired
    protected PlayerRepository playerRepository;

    @Autowired
    protected ApplicationEventPublisher applicationEventPublisher;

    /**
     * Creates and publishes an event where a list of players has changed
     *
     * @param tournamentId The id of the tournament the players are enrolled in
     */
    public void createAndPublishPlayerListChangeEvent(int tournamentId) {
        boolean tournamentHasStarted = tournamentRepository.getTournament(tournamentId).isActive();
        List<Player> players = playerRepository.getPlayersInTournament(tournamentId);
        List<Player> leaderBoard = playerRepository.getLeaderBoard(tournamentId);
        applicationEventPublisher.publishEvent(new PlayerListChangeEvent(this, players, leaderBoard,
                tournamentHasStarted, tournamentId));
    }

    /**
     * Creates and publishes an event where a player have been removed from a tournament
     *
     * @param playerId  The id of the removed player
     * @param message   The message sent to the removed player
     * @param wasKicked A boolean value to tell if the player was kicked.
     */
    public void createAndSendPlayerRemovedEvent(int playerId, String message, Boolean wasKicked) {
        PlayerRemovedEvent playerRemovedEvent = new PlayerRemovedEvent(this, playerId, message, wasKicked);
        applicationEventPublisher.publishEvent(playerRemovedEvent);
    }

    /**
     * Creates and publishes an event where a player has been added to a tournament
     *
     * @param tournamentId The id of the tournament the player has been added to.
     */
    public void createAndPublishNewPlayerAddedEvent(int tournamentId) {
        boolean tournamentHasStarted = tournamentRepository.getTournament(tournamentId).isActive();
        List<Player> players = playerRepository.getPlayersInTournament(tournamentId);
        List<Player> leaderBoard = playerRepository.getLeaderBoard(tournamentId);
        applicationEventPublisher.publishEvent(new NewPlayerAddedEvent(this, players, leaderBoard, tournamentId,
                tournamentHasStarted));
    }
}
