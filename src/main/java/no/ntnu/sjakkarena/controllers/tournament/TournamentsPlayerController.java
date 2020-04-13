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

@Controller
public class TournamentsPlayerController {

    @Autowired
    private TournamentsPlayerService tournamentsPlayerService;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private JSONCreator jsonCreator;

    @MessageMapping("/tournament/players")
    public void getPlayers(Authentication authentication){
        int tournamentId = WebSocketSession.getUserId(authentication);
        List<Player> players = tournamentsPlayerService.getPlayersInTournament(tournamentId);
        sendPlayerList(tournamentId, players);
    }

    @MessageMapping("/tournament/leaderboard")
    public void getLeaderBoard(Authentication authentication){
        int tournamentId = WebSocketSession.getUserId(authentication);
        List<Player> leaderBoard = tournamentsPlayerService.getLeaderBoard(tournamentId);
        sendLeaderBoard(tournamentId, leaderBoard);
    }

    /**
     * Sends all the names of the players in the tournament
     */
    @EventListener
    public void onPlayerListChange(PlayerListChangeEvent playerListChangeEvent) {
        if (playerListChangeEvent.hasTournamentStarted()) {
            sendLeaderBoard(playerListChangeEvent.getTournamentId(),
                    playerListChangeEvent.getLeaderBoard());
        } else {
            sendPlayerList(playerListChangeEvent.getTournamentId(),
                    playerListChangeEvent.getPlayers());
        }
    }

    /**
     * Sends the tournaments leaderboard
     */
    private void sendLeaderBoard(int tournamentId, List<Player> leaderBoard) {
        try {
            messageSender.sendToSubscriber(tournamentId, "/queue/tournament/leaderboard",
                    jsonCreator.writeValueAsString(leaderBoard));
        } catch (NotSubscribingException e) {
            e.printStackTrace();
        }
    }

    private void sendPlayerList(int tournamentId, List<Player> players) {
        try {
            messageSender.sendToSubscriber(tournamentId, "/queue/tournament/players",
                    jsonCreator.writeValueAsString(players));
        } catch (NotSubscribingException e) {
            e.printStackTrace();
        }
    }
}
