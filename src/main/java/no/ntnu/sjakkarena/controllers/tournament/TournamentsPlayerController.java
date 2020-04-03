package no.ntnu.sjakkarena.controllers.tournament;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.services.tournament.TournamentsPlayerService;
import no.ntnu.sjakkarena.subscriberhandler.TournamentSubscriberHandler;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TournamentsPlayerController {

    @Autowired
    private TournamentSubscriberHandler tournamentSubscriberHandler;

    @Autowired
    private TournamentsPlayerService tournamentsPlayerService;

    @MessageMapping("/tournament/players")
    public void getPlayers(Authentication authentication){
        int tournamentId = WebSocketSession.getUserId(authentication);
        List<Player> players = tournamentsPlayerService.getPlayersInTournament(tournamentId);
        tournamentSubscriberHandler.sendPlayerList(tournamentId, players);
    }

    @MessageMapping("/tournament/leaderboard")
    public void getLeaderBoard(Authentication authentication){
        int tournamentId = WebSocketSession.getUserId(authentication);
        List<Player> leaderBoard = tournamentsPlayerService.getLeaderBoard(tournamentId);
        tournamentSubscriberHandler.sendLeaderBoard(tournamentId, leaderBoard);
    }
}
