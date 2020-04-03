package no.ntnu.sjakkarena.controllers.player;

import no.ntnu.sjakkarena.services.player.PlayersTournamentService;
import no.ntnu.sjakkarena.subscriberhandler.PlayerSubscriberHandler;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class PlayersTournamentController {

    @Autowired
    private PlayerSubscriberHandler playerSubscriberHandler;

    @Autowired
    private PlayersTournamentService playersTournamentService;

    @MessageMapping("/player/tournament-active")
    public void isTournamentActive(Authentication authentication){
        int playerId = WebSocketSession.getUserId(authentication);
        playerSubscriberHandler.informPlayerAboutTournamentState(playerId,
                playersTournamentService.isTournamentActive(playerId));
    }
}
