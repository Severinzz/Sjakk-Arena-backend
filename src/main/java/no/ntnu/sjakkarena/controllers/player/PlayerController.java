package no.ntnu.sjakkarena.controllers.player;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.gameevents.ValidResultAddedEvent;
import no.ntnu.sjakkarena.events.playerevents.PlayerRemovedEvent;
import no.ntnu.sjakkarena.exceptions.NotSubscribingException;
import no.ntnu.sjakkarena.services.player.PlayerService;
import no.ntnu.sjakkarena.MessageSender;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

/**
 * This class handles Websocket communication regarding information about players
 */
@Controller
public class PlayerController{

    @Autowired
    private PlayerService playerService;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private JSONCreator jsonCreator;

    /**
     * Sends points to the player sending a message to the "/player/points" destination.
     *
     * @param authentication Authentication of the requesting user
     */
    @MessageMapping("/player/points")
    public void getPoints(Authentication authentication){
        int playerId = WebSocketSession.getUserId(authentication);
        Player player = playerService.getPlayer(playerId);
        sendPointsToPlayer(player);
    }

    /**
     * Responds to an event where a player has been removed from a tournament.
     * Sends a notification to the removed player.
     *
     * @param playerRemovedEvent An event where a player has been removed from a tournament
     */
    @EventListener
    public void onPlayerRemoved(PlayerRemovedEvent playerRemovedEvent){
        sendRemovedMessage(playerRemovedEvent.getPlayerId(), playerRemovedEvent.getRemoveReason());
    }

    /**
     * Sends a notification to player telling the player that he has been removed from
     * a tournament.
     *
     * @param playerId The id of the player who will be notified
     * @param message The message sent to the player removed from the tournament
     */
    private void sendRemovedMessage(int playerId, String message){
        try {
            messageSender.sendToSubscriber(playerId, "/queue/player/removed", message);
        } catch(NotSubscribingException e){
            e.printStackTrace();
        }
    }

    /**
     * Responds to an event where a valid result has been added to a game.
     * Sends points the players affected by the result
     *
     * @param validResultAddedEvent An event where a valid result has been added to a game
     */
    @EventListener
    public void onValidResultAdded(ValidResultAddedEvent validResultAddedEvent){
        sendPointsToPlayer(validResultAddedEvent.getWhitePlayer());
        sendPointsToPlayer(validResultAddedEvent.getBlackPlayer());
    }

    /**
     * Sends points to a player.
     *
     * @param player The player who will receive his/hers points
     */
    private void sendPointsToPlayer(Player player){
        try{
            messageSender.sendToSubscriber(player.getId(), "/queue/player/points",
                    jsonCreator.createResponseToPlayerPointsSubscriber(player.getPoints()));
        } catch(NotSubscribingException e){
            e.printStackTrace();
        }
    }
}
