package no.ntnu.sjakkarena.controllers.tournament;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.MessageSender;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.events.gameevents.GamesCreatedEvent;
import no.ntnu.sjakkarena.events.gameevents.InvalidResultEvent;
import no.ntnu.sjakkarena.events.gameevents.ValidResultAddedEvent;
import no.ntnu.sjakkarena.exceptions.NotSubscribingException;
import no.ntnu.sjakkarena.services.tournament.TournamentsGameService;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.List;

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

    @MessageMapping("/tournament/games/invalid")
    public void getGamesWithInvalidResult(Authentication authentication){
        int tournamentId = WebSocketSession.getUserId(authentication);
        List<? extends Game> gamesWithInvalidResult = tournamentsGameService.getGamesWithInvalidResult(tournamentId);
        sendGamesWithInvalidResult(tournamentId, gamesWithInvalidResult);
    }

    private void sendGamesWithInvalidResult(int tournamentId, Object gamesWithInvalidResult){
        try{
            messageSender.sendToSubscriber(tournamentId, "/queue/tournament/games/invalid",
                    jsonCreator.writeValueAsString(gamesWithInvalidResult));
        } catch (NotSubscribingException e){
            e.printStackTrace();
        }
    }

    @EventListener
    public void onResultValidated(ValidResultAddedEvent validResultAddedEvent){
        int tournamentId = validResultAddedEvent.getTournamentId();
        Collection<? extends Game> games = tournamentsGameService.getActiveGames(tournamentId);
        sendActiveGamesList(tournamentId, games);
    }

    /**
     * @param gamesCreatedEvent
     */
    @EventListener
    public void onGamesCreation(GamesCreatedEvent gamesCreatedEvent) {
        sendActiveGamesList(gamesCreatedEvent.getTournamentId(), gamesCreatedEvent.getActiveGames());
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
        sendGamesWithInvalidResult(invalidResultEvent.getTournamentId(), invalidResultEvent.getGame());
    }
}
