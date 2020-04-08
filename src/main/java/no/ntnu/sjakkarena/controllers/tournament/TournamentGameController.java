package no.ntnu.sjakkarena.controllers.tournament;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.services.tournament.TournamentsGameService;
import no.ntnu.sjakkarena.subscriberhandler.TournamentSubscriberHandler;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.Collection;

@Controller
public class TournamentGameController {

    @Autowired
    private TournamentsGameService tournamentsGameService;

    @Autowired
    private TournamentSubscriberHandler tournamentSubscriberHandler;

    @MessageMapping("/tournament/active-games")
    public void getActiveGames(Authentication authentication){
        int tournamentId = WebSocketSession.getUserId(authentication);
        Collection<? extends Game> games = tournamentsGameService.getActiveGames(tournamentId);
        tournamentSubscriberHandler.sendActiveGamesList(tournamentId, games);
    }
}
