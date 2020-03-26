package no.ntnu.sjakkarena.controllers;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.services.TournamentService;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    private JSONCreator jsonCreator = new JSONCreator();

    @MessageMapping("/tournament/active")
    @SendToUser("/queue/tournament/active")
    public String isActive(Authentication authentication){
        int tournamentId = WebSocketSession.getUserId(authentication);
        return jsonCreator.createResponseToTournamentStateRequester(tournamentService.isTournamentActive(tournamentId));
    }
}
