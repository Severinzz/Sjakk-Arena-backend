package no.ntnu.sjakkarena.controllers;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.services.PlayerService;
import no.ntnu.sjakkarena.subscriberhandler.PlayerSubscriberHandler;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class PlayerController{

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerSubscriberHandler playerSubscriberHandler;

    private JSONCreator jsonCreator = new JSONCreator();

    @MessageMapping("/player/tournament-active")
    public void isTournamentActive(Authentication authentication){
        int playerId = WebSocketSession.getUserId(authentication);
        Player player = playerService.getPlayer(playerId);
        playerSubscriberHandler.informPlayerAboutTournamentState(player, playerService.isTournamentActive(playerId));
    }

    @MessageMapping("/player/points")
    public void getPoints(Authentication authentication){
        int playerId = WebSocketSession.getUserId(authentication);
        Player player = playerService.getPlayer(playerId);
        playerSubscriberHandler.sendPointsToPlayer(player);
    }
}
