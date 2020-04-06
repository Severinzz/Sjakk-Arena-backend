package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.adaptedmonrad.AdaptedMonrad;
import no.ntnu.sjakkarena.adaptedmonrad.AfterTournamentStartAdaptedMonrad;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.*;
import no.ntnu.sjakkarena.repositories.GameRepository;
import no.ntnu.sjakkarena.repositories.GameWithPlayerNamesRepository;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
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

    // TODO Remove field.
    private AdaptedMonrad adaptedMonrad;

    @Autowired
    private GameRepository gameRepository;

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

    @EventListener
    public void onNewPlayerAdd(NewPlayerAddedEvent newPlayerAddedEvent){
        if (newPlayerAddedEvent.hasTournamentStarted()) {
            this.adaptedMonrad = newPlayerAddedEvent.getAdaptedMonrad();
            manageNewGamesRequest(newPlayerAddedEvent.getTournamentId());
        }
    }

    @EventListener
    public void onTournamentStart(TournamentStartedEvent tournamentStartedEvent) {
        this.adaptedMonrad = tournamentStartedEvent.getAdaptedMonrad();
        manageNewGamesRequest(tournamentStartedEvent.getTournamentId());
    }


    public void onResultAdd(int playerId, int opponentId) {
        int tournamentId = playerRepository.getPlayer(playerId).getTournamentId();
        createAndPublishPlayerListChangeEvent(tournamentId);
        createAndPublishResultAddedEvent(playerId, opponentId);
        this.adaptedMonrad = new AfterTournamentStartAdaptedMonrad();
        manageNewGamesRequest(tournamentId);
    }

    private void createAndPublishResultAddedEvent(int requestingUserId, int opponentId) {
        Player requestingPlayer = playerRepository.getPlayer(requestingUserId);
        Player opponent = playerRepository.getPlayer(opponentId);
        applicationEventPublisher.publishEvent(new ResultAddedEvent(this, requestingPlayer, opponent));
    }

    private void manageNewGamesRequest(int tournamentId){
        List<Game> newGames = requestNewGames(tournamentId);
        gameRepository.addGames(newGames);
        createAndPublishNewGamesEvent(tournamentId);
    }

    private List<Game> requestNewGames(int tournamentId){
        List<Player> inActivePlayers = playerRepository.getPlayersWhoIsCurrentlyNotPlaying(tournamentId);
        List<Integer> availableTables = tournamentRepository.getAvailableTables(tournamentId);
        return adaptedMonrad.getNewGames(inActivePlayers, availableTables);
    }

    protected void onSuggestedResult(int opponentId, double result, int gameId) {
        applicationEventPublisher.publishEvent(new ResultSuggestedEvent(this, gameId, opponentId, result));
    }

    protected void onResultInvalidation(int gameId){
        Game game = gameWithPlayerNamesRepository.getGame(gameId);
        Player player = playerRepository.getPlayer(game.getWhitePlayerId());
        applicationEventPublisher.publishEvent(new InvalidResultEvent(this, game, player.getTournamentId()));
    }
}
