package no.ntnu.sjakkarena.controllers.websocketcontrollers;

import no.ntnu.sjakkarena.subscriberhandler.TournamentSubscriberHandler;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;


/**
 * Handles websocket sessions with tournaments
 */
@Controller
public class TournamentWebSocketController {

    @Autowired
    private TournamentSubscriberHandler tournamentSubscriberHandler;

    /**
     * Returns all the names of the players in the tournament
     *
     * @return A json with the player ids mapped to their names
     */
    @MessageMapping("/tournament/players")
    public void getPlayers(Authentication authentication) {
        tournamentSubscriberHandler.sendPlayerList(WebSocketSession.getUserId(authentication));
    }


    /**
     * Returns the leaderboard of the requesting tournament
     *
     * @return the leaderboard of the requesting tournament
     */
    @MessageMapping("/tournament/leaderboard")
    public void getLeaderboard(Authentication authentication) {
        tournamentSubscriberHandler.sendLeaderBoard(WebSocketSession.getUserId(authentication));
    }
}
