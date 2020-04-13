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

@Controller
public class PlayerController{

    @Autowired
    private PlayerService playerService;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private JSONCreator jsonCreator;

    @MessageMapping("/player/points")
    public void getPoints(Authentication authentication){
        int playerId = WebSocketSession.getUserId(authentication);
        Player player = playerService.getPlayer(playerId);
        sendPointsToPlayer(player);
    }

    @EventListener
    public void onPlayerRemoved(PlayerRemovedEvent playerRemovedEvent){
        try {
            messageSender.sendToSubscriber(
                    playerRemovedEvent.getPlayerId(), "/queue/player/removed",
                    playerRemovedEvent.getRemoveReason());
        } catch(NotSubscribingException e){
            e.printStackTrace();
        }
    }

    @EventListener
    public void onResultAdded(ValidResultAddedEvent validResultAddedEvent){
        sendPointsToPlayer(validResultAddedEvent.getPlayer1());
        sendPointsToPlayer(validResultAddedEvent.getPlayer2());

    }

    private void sendPointsToPlayer(Player player){
        try{
            messageSender.sendToSubscriber(player.getId(), "/queue/player/points",
                    jsonCreator.createResponseToPlayerPointsSubscriber(player.getPoints()));
        } catch(NotSubscribingException e){
            e.printStackTrace();
        }
    }
}
