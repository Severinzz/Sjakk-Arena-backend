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
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class PlayersGameController {

    @Autowired
    private PlayersGameService playersGameService;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private JSONCreator jsonCreator;

    @MessageMapping("/player/active-game")
    public void getActiveGame(Authentication authentication){
        int playerId = WebSocketSession.getUserId(authentication);
        Game activeGame = playersGameService.getActiveGame(playerId);
        sendGame(activeGame, playerId);
    }

    /**
     *
     * @param gamesCreatedEvent
     */
    @EventListener
    public void onGamesCreated(GamesCreatedEvent gamesCreatedEvent) {
        for (Game game : gamesCreatedEvent.getActiveGames()){
            sendGameToWhiteAndBlackPlayer(game);
        }
    }

    @EventListener
    public void onResultAdded(ValidResultAddedEvent validResultAddedEvent){
        sendValidResultInformationToPlayer(validResultAddedEvent.getPlayer1().getId());
        sendValidResultInformationToPlayer(validResultAddedEvent.getPlayer2().getId());
    }

    @EventListener
    public void onResultSuggested(ResultSuggestedEvent resultSuggestedEvent){
        sendResultInformationToPlayer(resultSuggestedEvent.getOpponentId(), resultSuggestedEvent.getResult(), resultSuggestedEvent.getGameId(),
                false, false);
    }

    @EventListener
    public void onResultInvalidated(InvalidResultEvent invalidResultEvent){
        Game game = invalidResultEvent.getGame();
        sendInvalidResultInformationToPlayer(game.getWhitePlayerId(), game);
        sendInvalidResultInformationToPlayer(game.getBlackPlayerId(), game);
    }

    @EventListener
    public void onPlayerRemoved(PlayerRemovedEvent playerRemovedEvent){
        int opponent = playersGameService.getOpponent(playerRemovedEvent.getPlayerId());
        playersGameService.endGameDueToPlayerRemoved(playerRemovedEvent.getPlayerId());
        sendGame(Game.emptyInactiveGame(), opponent);
    }

    private void sendGameToWhiteAndBlackPlayer(Game game){
        sendGame(game, game.getWhitePlayerId());
        sendGame(game, game.getBlackPlayerId());
    }

    private void sendGame(Game game, int playerId) {
        try {
            messageSender.sendToSubscriber(playerId, "/queue/player/active-game",
                    jsonCreator.filterGameInformationAndReturnAsJson(game, playerId));
        }
        catch(NotSubscribingException e){
            e.printStackTrace();
        }
    }

    private void sendValidResultInformationToPlayer(int playerId){
        sendResultInformationToPlayer(playerId, null, null, false, true);
    }

    private void sendInvalidResultInformationToPlayer(int playerId, Game game){
        sendResultInformationToPlayer(playerId, null, game.getGameId(),
                true, false);
    }

    private void sendResultInformationToPlayer(int playerId, Double result, Integer gameId, boolean opponentsDisagree,
                                               boolean validResult){
        try{
            messageSender.sendToSubscriber(playerId, "/queue/player/result",
                    jsonCreator.createResponseToResultSubscriber(result,
                            gameId, opponentsDisagree, validResult));
        } catch(NotSubscribingException e){
            e.printStackTrace();
        }
    }
}
