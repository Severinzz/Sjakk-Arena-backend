package no.ntnu.sjakkarena.controllers.player;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.events.gameevents.GamesCreatedEvent;
import no.ntnu.sjakkarena.events.gameevents.InvalidResultEvent;
import no.ntnu.sjakkarena.events.gameevents.ResultSuggestedEvent;
import no.ntnu.sjakkarena.events.gameevents.ValidResultAddedEvent;
import no.ntnu.sjakkarena.events.playerevents.PlayerRemovedEvent;
import no.ntnu.sjakkarena.exceptions.NotSubscribingException;
import no.ntnu.sjakkarena.services.player.PlayersGameService;
import no.ntnu.sjakkarena.MessageSender;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

/**
 * This class handles WebSocket communication regarding information about players' games
 */
@Controller
public class PlayersGameController {

    @Autowired
    private PlayersGameService playersGameService;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private JSONCreator jsonCreator;



    /**
     * Sends the active game of the player sending a message to the "/player/active-game" destination.
     *
     * @param authentication Authentication of the requesting user
     */
    @MessageMapping("/player/active-game")
    public void getActiveGame(Authentication authentication) {
        int playerId = WebSocketSession.getUserId(authentication);
        try {
            Game activeGame = playersGameService.getActiveGame(playerId);
            sendGame(activeGame, playerId);
        } catch (EmptyResultDataAccessException e) {
            throw e;
        }
    }

    /**
     * Responds to an event where games have been created.
     * Sends the games to their respective players.
     *
     * @param gamesCreatedEvent An event where games have been created
     */
    @EventListener
    public void onGamesCreated(GamesCreatedEvent gamesCreatedEvent) {
        for (Game game : gamesCreatedEvent.getCreatedGames()) {
            sendGameToBothPlayers(game);
        }
    }

    /**
     * Responds to an event where a valid result has been added to a game.
     * Notifies the players playing the game that the result is added and is valid.
     *
     * @param validResultAddedEvent
     */
    @EventListener
    public void onValidResultAdded(ValidResultAddedEvent validResultAddedEvent) {
        sendValidResultInformationToPlayer(validResultAddedEvent.getWhitePlayer().getId());
        sendValidResultInformationToPlayer(validResultAddedEvent.getBlackPlayer().getId());
    }

    /**
     * Responds to an event where a result is suggested.
     * Notifies players that a result is suggested.
     *
     * @param resultSuggestedEvent An event where a result is suggested
     */
    @EventListener
    public void onResultSuggested(ResultSuggestedEvent resultSuggestedEvent) {
        sendResultInformationToPlayer(resultSuggestedEvent.getOpponentId(), resultSuggestedEvent.getResult(), resultSuggestedEvent.getGameId(),
                false, false);
    }

    /**
     * Responds to an event where a result is invalidated.
     * Notifies players that the result is invalidated.
     *
     * @param invalidResultEvent An event where a result is invalidated.
     */
    @EventListener
    public void onResultInvalidated(InvalidResultEvent invalidResultEvent) {
        Game game = invalidResultEvent.getGame();
        sendInvalidResultInformationToPlayer(game.getWhitePlayerId(), game);
        sendInvalidResultInformationToPlayer(game.getBlackPlayerId(), game);
    }

    /**
     * Responds to an event where a player is removed from a tournament.
     * Ends the game the removed player is playing and notifies the opponent that game is ended.
     *
     * @param playerRemovedEvent An event where a player is removed from a tournament
     */
    @EventListener
    public void onPlayerRemoved(PlayerRemovedEvent playerRemovedEvent) {
        int opponent = playersGameService.getOpponent(playerRemovedEvent.getPlayerId());
        playersGameService.endGameDueToPlayerRemoval(playerRemovedEvent.getPlayerId());
        sendGame(Game.emptyInactiveGame(), opponent);
    }

    /**
     * Sends a game to it's players
     *
     * @param game A game
     */
    private void sendGameToBothPlayers(Game game) {
        sendGame(game, game.getWhitePlayerId());
        sendGame(game, game.getBlackPlayerId());
    }

    /**
     * Sends a game to a player
     *
     * @param game     The game to be sent
     * @param playerId The id of the player who will receive the game
     */
    private void sendGame(Game game, int playerId) {
        try {
            messageSender.sendToSubscriber(playerId, "/queue/player/active-game",
                    jsonCreator.filterGameInformationAndReturnAsJson(game, playerId));
        } catch (NotSubscribingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends information about a valid result to a player
     *
     * @param playerId The id of the player who will receive the valid result information
     */
    private void sendValidResultInformationToPlayer(int playerId) {
        sendResultInformationToPlayer(playerId, null, null, false, true);
    }

    /**
     * Sends information about a invalid result to a player
     *
     * @param playerId The id of the player who will receive the invalid result information
     */
    private void sendInvalidResultInformationToPlayer(int playerId, Game game) {
        sendResultInformationToPlayer(playerId, null, game.getGameId(),
                true, false);
    }

    /**
     * Sends information about results to a player
     *
     * @param playerId The player who will receive the information
     * @param result A result e.g 1, 0.5 or 0
     * @param gameId The id of the game the result is associated with
     * @param opponentsDisagree Whether the opponents disagree
     * @param validResult Whether the result is valid
     */
    private void sendResultInformationToPlayer(int playerId, Double result, Integer gameId, boolean opponentsDisagree,
                                               boolean validResult) {
        try {
            messageSender.sendToSubscriber(playerId, "/queue/player/result",
                    jsonCreator.createResponseToResultSubscriber(result,
                            gameId, opponentsDisagree, validResult));
        } catch (NotSubscribingException e) {
            e.printStackTrace();
        }
    }
}
