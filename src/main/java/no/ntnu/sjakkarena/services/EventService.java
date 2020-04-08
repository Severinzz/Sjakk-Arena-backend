package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.gameevents.GamesCreatedEvent;
import no.ntnu.sjakkarena.events.playerevents.PlayerListChangeEvent;
import no.ntnu.sjakkarena.events.tournamentevents.TournamentEndedEvent;
import no.ntnu.sjakkarena.events.tournamentevents.TournamentStartedEvent;
import no.ntnu.sjakkarena.repositories.GameWithPlayerNamesRepository;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class EventService {
    @Autowired
    protected PlayerRepository playerRepository;

    @Autowired
    protected ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    protected TournamentRepository tournamentRepository;

    @Autowired
    protected GameWithPlayerNamesRepository gameWithPlayerNamesRepository;

    protected void createAndPublishPlayerListChangeEvent(int tournamentId) {
        boolean tournamentHasStarted = tournamentRepository.getTournament(tournamentId).isActive();
        List<Player> players = playerRepository.getPlayersInTournament(tournamentId);
        List<Player> leaderBoard = playerRepository.getLeaderBoard(tournamentId);
        applicationEventPublisher.publishEvent(new PlayerListChangeEvent(this, players, leaderBoard,
                tournamentHasStarted, tournamentId));
    }


    protected void createAndPublishNewGamesEvent(int tournamentId) {
        List<? extends Game> activeGames =  gameWithPlayerNamesRepository.getActiveGames(tournamentId);
        GamesCreatedEvent gamesCreatedEvent = new GamesCreatedEvent(this, activeGames, tournamentId);
        applicationEventPublisher.publishEvent(gamesCreatedEvent);
    }

    protected void createAndPublishTournamentStartedEvent(int tournamentId){
        List<Player> players = playerRepository.getPlayersInTournament(tournamentId);
        applicationEventPublisher.publishEvent(new TournamentStartedEvent(this, tournamentId, players));
    }

    protected void createAndPublishTournamentEndedEvent(int tournamentId){
        List<Player> players = playerRepository.getPlayersInTournament(tournamentId);
        applicationEventPublisher.publishEvent((new TournamentEndedEvent(this, tournamentId, players)));
    }
}
