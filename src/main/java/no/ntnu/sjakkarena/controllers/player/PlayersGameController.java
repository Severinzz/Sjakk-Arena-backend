package no.ntnu.sjakkarena.controllers.player;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.services.player.PlayersGameService;
import no.ntnu.sjakkarena.subscriberhandler.PlayerSubscriberHandler;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class PlayersGameController {

    @Autowired
    private PlayersGameService playersGameService;

    @Autowired
    private PlayerSubscriberHandler playerSubscriberHandler;

    @MessageMapping("/player/active-game")
    public void getActiveGame(Authentication authentication){
        int playerId = WebSocketSession.getUserId(authentication);
        Game activeGame = playersGameService.getActiveGame(playerId);
        playerSubscriberHandler.sendGame(activeGame, playerId);
    }
}
