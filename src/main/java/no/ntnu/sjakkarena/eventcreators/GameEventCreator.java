package no.ntnu.sjakkarena.eventcreators;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.gameevents.GamesCreatedEvent;
import no.ntnu.sjakkarena.events.gameevents.InvalidResultEvent;
import no.ntnu.sjakkarena.events.gameevents.ResultAddedEvent;
import no.ntnu.sjakkarena.events.gameevents.ResultSuggestedEvent;
import no.ntnu.sjakkarena.repositories.GameWithPlayerNamesRepository;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameEventCreator {

    @Autowired
    protected GameWithPlayerNamesRepository gameWithPlayerNamesRepository;

    @Autowired
    protected PlayerRepository playerRepository;

    @Autowired
    protected ApplicationEventPublisher applicationEventPublisher;

    public void createAndPublishGamesCreatedEvent(int tournamentId) {
        List<? extends Game> activeGames =  gameWithPlayerNamesRepository.getActiveGames(tournamentId);
        GamesCreatedEvent gamesCreatedEvent = new GamesCreatedEvent(this, activeGames, tournamentId);
        applicationEventPublisher.publishEvent(gamesCreatedEvent);
    }

    public void createAndPublishResultAddedEvent(int requestingUserId, int opponentId) {
        Player requestingPlayer = playerRepository.getPlayer(requestingUserId);
        Player opponent = playerRepository.getPlayer(opponentId);
        applicationEventPublisher.publishEvent(new ResultAddedEvent(this, requestingPlayer, opponent));
    }

    public void createAndPublishResultSuggestedEvent(int opponentId, double result, int gameId) {
        applicationEventPublisher.publishEvent(new ResultSuggestedEvent(this, gameId, opponentId, result));
    }

    public void createAnPublishInvalidResultEvent(int gameId){
        Game game = gameWithPlayerNamesRepository.getGame(gameId);
        Player player = playerRepository.getPlayer(game.getWhitePlayerId());
        applicationEventPublisher.publishEvent(new InvalidResultEvent(this, game, player.getTournamentId()));
    }

}
