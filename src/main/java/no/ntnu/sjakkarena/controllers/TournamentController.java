package no.ntnu.sjakkarena.controllers;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.services.TournamentService;
import no.ntnu.sjakkarena.subscriberhandler.TournamentSubscriberHandler;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private TournamentSubscriberHandler tournamentSubscriberHandler;

    @MessageMapping("/tournament/active")
    public void isActive(Authentication authentication){
        int tournamentId = WebSocketSession.getUserId(authentication);
        boolean active = tournamentService.isTournamentActive(tournamentId);
        tournamentSubscriberHandler.sendActiveStateToTournament(tournamentId, active);
    }

    @MessageMapping("/tournament/players")
    public void getPlayers(Authentication authentication){
        int tournamentId = WebSocketSession.getUserId(authentication);
        List<Player> players = tournamentService.getPlayersInTournament(tournamentId);
        tournamentSubscriberHandler.sendPlayerList(tournamentId, players);
    }

    @MessageMapping("/tournament/leaderboard")
    public void getLeaderBoard(Authentication authentication){
        int tournamentId = WebSocketSession.getUserId(authentication);
        List<Player> leaderBoard = tournamentService.getLeaderBoard(tournamentId);
        tournamentSubscriberHandler.sendLeaderBoard(tournamentId, leaderBoard);
    }
}
