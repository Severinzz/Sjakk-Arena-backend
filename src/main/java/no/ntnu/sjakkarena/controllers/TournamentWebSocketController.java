package no.ntnu.sjakkarena.controllers;

import no.ntnu.sjakkarena.DBChangeNotifier;
import no.ntnu.sjakkarena.data.User;
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
    private DBChangeNotifier dbChangeNotifier;

    /**
     * Returns all the names of the players in the tournament
     *
     * @return A json with the player ids mapped to their names
     */
    @MessageMapping("/tournament/players")
    public void getPlayers(Authentication authentication) {
        dbChangeNotifier.notifyUpdatedPlayerList(getUserId(authentication));
    }


    /**
     * Returns the leaderboard of the requesting tournament
     *
     * @return the leaderboard of the requesting tournament
     */
    @MessageMapping("/tournament/leaderboard")
    public void getLeaderboard(Authentication authentication) {
        dbChangeNotifier.notifyUpdatedLeaderboard(getUserId(authentication));
    }

    private int getUserId(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}