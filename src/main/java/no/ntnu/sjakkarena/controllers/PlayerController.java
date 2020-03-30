package no.ntnu.sjakkarena.controllers;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.services.PlayerService;
import no.ntnu.sjakkarena.subscriberhandler.SubscriberHandler;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class PlayerController extends SubscriberHandler {

    @Autowired
    private PlayerService playerService;

    private JSONCreator jsonCreator = new JSONCreator();

    @MessageMapping("/player/tournament-active")
    public void isTournamentActive(Authentication authentication){
        int playerId = WebSocketSession.getUserId(authentication);
        sendToSubscriber(playerId, "/queue/player/tournament-active",
                playerService.isTournamentActive(playerId));
    }
}
