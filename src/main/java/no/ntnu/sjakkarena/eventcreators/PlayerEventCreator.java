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

@Component
public class PlayerEventCreator {

    @Autowired
    protected TournamentRepository tournamentRepository;

    @Autowired
    protected PlayerRepository playerRepository;

    @Autowired
    protected ApplicationEventPublisher applicationEventPublisher;

    public void createAndPublishPlayerListChangeEvent(int tournamentId) {
        boolean tournamentHasStarted = tournamentRepository.getTournament(tournamentId).isActive();
        List<Player> players = playerRepository.getPlayersInTournament(tournamentId);
        List<Player> leaderBoard = playerRepository.getLeaderBoard(tournamentId);
        applicationEventPublisher.publishEvent(new PlayerListChangeEvent(this, players, leaderBoard,
                tournamentHasStarted, tournamentId));
    }

    public void createAndSendPlayerRemovedEvent(int playerId, String msg) {
        PlayerRemovedEvent playerRemovedEvent = new PlayerRemovedEvent(this, playerId, msg);
        applicationEventPublisher.publishEvent(playerRemovedEvent);
    }

    public void createAndPublishNewPlayerAddedEvent(int tournamentId){
        boolean tournamentHasStarted = tournamentRepository.getTournament(tournamentId).isActive();
        List<Player> players = playerRepository.getPlayersInTournament(tournamentId);
        List<Player> leaderBoard = playerRepository.getLeaderBoard(tournamentId);
        applicationEventPublisher.publishEvent(new NewPlayerAddedEvent(this, players, leaderBoard, tournamentId,
                tournamentHasStarted));
    }
}
