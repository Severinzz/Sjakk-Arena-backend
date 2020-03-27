package no.ntnu.sjakkarena.controllers;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.services.TournamentService;
import no.ntnu.sjakkarena.subscriberhandler.SubscriberHandler;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TournamentController extends SubscriberHandler {

    @Autowired
    private TournamentService tournamentService;

    private JSONCreator jsonCreator = new JSONCreator();

    @Autowired
    protected SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/tournament/active")
    public void isActive(Authentication authentication){
        int tournamentId = WebSocketSession.getUserId(authentication);
        sendToSubscriber(tournamentId, "/queue/tournament/active",
                jsonCreator.createResponseToTournamentStateRequester(tournamentService.isTournamentActive(tournamentId)));
    }

    @MessageMapping("/tournament/players")
    public void getPlayers(Authentication authentication){
        int tournamentId = WebSocketSession.getUserId(authentication);
        List<Player> players = tournamentService.getPlayersInTournament(tournamentId);
        sendToSubscriber(tournamentId, "/queue/tournament/players",
                jsonCreator.writeValueAsString(players));
    }

    @MessageMapping("/tournament/leaderboard")
    public void getLeaderBoard(Authentication authentication){
        int tournamentId = WebSocketSession.getUserId(authentication);
        List<Player> players = tournamentService.getLeaderBoard(tournamentId);
        sendToSubscriber(tournamentId, "/queue/tournament/leaderboard",
                jsonCreator.writeValueAsString(players));
    }
}
