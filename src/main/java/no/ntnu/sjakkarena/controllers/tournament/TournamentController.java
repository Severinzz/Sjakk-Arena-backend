package no.ntnu.sjakkarena.controllers.tournament;

import no.ntnu.sjakkarena.services.tournament.TournamentService;
import no.ntnu.sjakkarena.subscriberhandler.TournamentSubscriberHandler;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

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
}
