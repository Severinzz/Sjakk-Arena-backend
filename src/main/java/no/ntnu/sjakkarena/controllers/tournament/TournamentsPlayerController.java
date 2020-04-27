package no.ntnu.sjakkarena.controllers.tournament;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.playerevents.PlayerListChangeEvent;
import no.ntnu.sjakkarena.exceptions.NotSubscribingException;
import no.ntnu.sjakkarena.services.tournament.TournamentsPlayerService;
import no.ntnu.sjakkarena.MessageSender;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * This class handles WebSocket communication regarding information about tournaments' players
 */
@Controller
public class TournamentsPlayerController {

    @Autowired
    private TournamentsPlayerService tournamentsPlayerService;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private JSONCreator jsonCreator;

    /**
     * Sends a tournament's players to the tournament host sending a message to the "/tournament/players" destination.
     *
     * @param authentication Authentication of the requesting user
     */
    @MessageMapping("/tournament/players")
    public void getPlayers(Authentication authentication){
        int tournamentId = WebSocketSession.getUserId(authentication);
        List<Player> players = tournamentsPlayerService.getPlayersInTournament(tournamentId);
        sendPlayers(tournamentId, players);
    }

    /**
     * Sends a tournament's leaederboard to the tournament host sending a message to the "/tournament/leaderboard"
     * destination.
     *
     * @param authentication Authentication of the requesting user
     */
    @MessageMapping("/tournament/leaderboard")
    public void getLeaderBoard(Authentication authentication){
        int tournamentId = WebSocketSession.getUserId(authentication);
        List<Player> leaderBoard = tournamentsPlayerService.getLeaderBoard(tournamentId);
        sendLeaderBoard(tournamentId, leaderBoard);
    }

    /**
     * Responds to an event where a list of players has changed.
     * Sends all the names of the players in the tournament.
     *
     * @param playerListChangeEvent An event where a list of player has changed
     */
    @EventListener
    public void onPlayerListChange(PlayerListChangeEvent playerListChangeEvent) {
        if (playerListChangeEvent.hasTournamentStarted()) {
            sendLeaderBoard(playerListChangeEvent.getTournamentId(),
                    playerListChangeEvent.getLeaderBoard());
        } else {
            sendPlayers(playerListChangeEvent.getTournamentId(),
                    playerListChangeEvent.getPlayers());
        }
    }

    /**
     * Sends a tournament's leader board
     *
     * @param tournamentId The id of the tournament that will receive the leader board
     * @param leaderBoard The leader board to be sent
     */
    private void sendLeaderBoard(int tournamentId, List<Player> leaderBoard) {
        try {
            messageSender.sendToSubscriber(tournamentId, "/queue/tournament/leaderboard",
                    jsonCreator.writeValueAsString(leaderBoard));
        } catch (NotSubscribingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a list of players to a tournament
     *
     * @param tournamentId The if of the tournament that will receive the list of players
     * @param players The players to be sent
     */
    private void sendPlayers(int tournamentId, List<Player> players) {
        try {
            messageSender.sendToSubscriber(tournamentId, "/queue/tournament/players",
                    jsonCreator.writeValueAsString(players));
        } catch (NotSubscribingException e) {
            e.printStackTrace();
        }
    }
}
