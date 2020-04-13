package no.ntnu.sjakkarena.controllers.tournament;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.events.gameevents.GamesCreatedEvent;
import no.ntnu.sjakkarena.events.gameevents.InvalidResultEvent;
import no.ntnu.sjakkarena.exceptions.NotSubscribingException;
import no.ntnu.sjakkarena.services.tournament.TournamentsGameService;
import no.ntnu.sjakkarena.MessageSender;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.Collection;

@Controller
public class TournamentGameController {

    @Autowired
    private TournamentsGameService tournamentsGameService;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private JSONCreator jsonCreator;

    @MessageMapping("/tournament/active-games")
    public void getActiveGames(Authentication authentication){
        int tournamentId = WebSocketSession.getUserId(authentication);
        Collection<? extends Game> games = tournamentsGameService.getActiveGames(tournamentId);
        sendActiveGamesList(tournamentId, games);
    }

    /**
     * @param gamesCreatedEvent
     */
    @EventListener
    public void onGamesCreation(GamesCreatedEvent gamesCreatedEvent) {
        try {
            messageSender.sendToSubscriber(gamesCreatedEvent.getTournamentId(), "/queue/tournament/active-games",
                    jsonCreator.writeValueAsString(gamesCreatedEvent.getActiveGames()));
        } catch (NotSubscribingException e) {
            e.printStackTrace();
        }
    }

    private void sendActiveGamesList(int tournamentId, Collection<? extends Game> games){
        try {
            messageSender.sendToSubscriber(tournamentId, "/queue/tournament/active-games",
                    jsonCreator.writeValueAsString(games));
        } catch (NotSubscribingException e) {
            e.printStackTrace();
        }
    }

    @EventListener
    public void onResultInvalidation(InvalidResultEvent invalidResultEvent){
        try{
            messageSender.sendToSubscriber(invalidResultEvent.getTournamentId(), "/queue/tournament/games/invalid",
                    jsonCreator.writeValueAsString(invalidResultEvent.getGame()));
        } catch (NotSubscribingException e){
            e.printStackTrace();
        }
    }
}
