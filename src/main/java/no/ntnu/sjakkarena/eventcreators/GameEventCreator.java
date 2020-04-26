package no.ntnu.sjakkarena.eventcreators;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.gameevents.GamesCreatedEvent;
import no.ntnu.sjakkarena.events.gameevents.InvalidResultEvent;
import no.ntnu.sjakkarena.events.gameevents.ValidResultAddedEvent;
import no.ntnu.sjakkarena.events.gameevents.ResultSuggestedEvent;
import no.ntnu.sjakkarena.repositories.GameWithPlayerNamesRepository;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Creates Game Events.
 */
@Component
public class GameEventCreator {

    @Autowired
    protected GameWithPlayerNamesRepository gameWithPlayerNamesRepository;

    @Autowired
    protected PlayerRepository playerRepository;

    @Autowired
    protected ApplicationEventPublisher applicationEventPublisher;

    /**
     * Creates and publishes an event where games have been created
     *
     * @param tournamentId The id of the tournament which the newly created games belong to
     */
    public void createAndPublishGamesCreatedEvent(int tournamentId) {
        List<? extends Game> activeGames = gameWithPlayerNamesRepository.getActiveGames(tournamentId);
        GamesCreatedEvent gamesCreatedEvent = new GamesCreatedEvent(this, activeGames, tournamentId);
        applicationEventPublisher.publishEvent(gamesCreatedEvent);
    }

    /**
     * Creates and publishes an event where a valid result is added
     *
     * @param gameId The game which the valid result is associated with
     */
    public void createAndPublishValidResultAddedEvent(int gameId) {
        Game game = gameWithPlayerNamesRepository.getGame(gameId);
        Player whitePlayer = playerRepository.getPlayer(game.getWhitePlayerId());
        Player blackPlayer = playerRepository.getPlayer(game.getBlackPlayerId());
        applicationEventPublisher.publishEvent(new ValidResultAddedEvent(this, whitePlayer, blackPlayer,
                whitePlayer.getTournamentId()));
    }

    /**
     * Creates and publishes an event where a result has been suggested by a player
     *
     * @param opponentId The id of the player who will receive the result suggestion
     * @param result     The result being suggested
     * @param gameId     The id of the game the suggested result is associated with
     */
    public void createAndPublishResultSuggestedEvent(int opponentId, double result, int gameId) {
        applicationEventPublisher.publishEvent(new ResultSuggestedEvent(this, gameId, opponentId, result));
    }

    /**
     * Creates and publishes an event where a result has been declared invalid
     *
     * @param gameId The id of the game the invalid result is associated with
     */
    public void createAnPublishInvalidResultEvent(int gameId) {
        Game game = gameWithPlayerNamesRepository.getGame(gameId);
        Player player = playerRepository.getPlayer(game.getWhitePlayerId());
        applicationEventPublisher.publishEvent(new InvalidResultEvent(this, game, player.getTournamentId()));
    }
}
