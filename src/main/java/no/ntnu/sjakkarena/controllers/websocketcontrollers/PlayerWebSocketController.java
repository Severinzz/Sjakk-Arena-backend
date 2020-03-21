package no.ntnu.sjakkarena.controllers.websocketcontrollers;

import no.ntnu.sjakkarena.subscriberhandler.PlayerSubscriberHandler;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

/**
 * Handles websocket sessions with tournaments
 */
@Controller
public class PlayerWebSocketController {

    @Autowired
    private PlayerSubscriberHandler playerSubscriberHandler;

    /**
     * Returns a list of a player's games
     *
     * @return a list of a player's games
     */
    @MessageMapping(value = "/player/games")
    public void getGames(Authentication authentication) {
        //playerSubscriberHandler.handleNewGamesEvent(WebSocketSession.getUserId(authentication));
    }
}
