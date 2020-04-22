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

/**
 * This class handles WebSocket communication regarding information about tournaments' games
 */
@Controller
public class TournamentsGameController {

    @Autowired
    private TournamentsGameService tournamentsGameService;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private JSONCreator jsonCreator;

    /**
     * Sends active games to the tournament sending a message to the "/tournament/active-games" destination
     *
     * @param authentication Authentication of the requesting user
     */
    @MessageMapping("/tournament/active-games")
    public void getActiveGames(Authentication authentication) {
        int tournamentId = WebSocketSession.getUserId(authentication);
        Collection<? extends Game> games = tournamentsGameService.getActiveGames(tournamentId);
        sendActiveGames(tournamentId, games);
    }

    /**
     * Sends invalid games to the tournament sending a message to the "/tournament/games/invalid" destination
     *
     * @param authentication Authentication of the requesting user
     */
    @MessageMapping("/tournament/games/invalid")
    public void getGamesWithInvalidResult(Authentication authentication) {
        int tournamentId = WebSocketSession.getUserId(authentication);
        List<? extends Game> gamesWithInvalidResult = tournamentsGameService.getGamesWithInvalidResult(tournamentId);
        sendGamesWithInvalidResult(tournamentId, gamesWithInvalidResult);
    }

    /**
     * Sends games with an invalid result to a tournament
     *
     * @param tournamentId           The id of the tournament that will receive the games with a invalid result
     * @param gamesWithInvalidResult The games with invalid result.
     */
    private void sendGamesWithInvalidResult(int tournamentId, Object gamesWithInvalidResult) {
        try {
            messageSender.sendToSubscriber(tournamentId, "/queue/tournament/games/invalid",
                    jsonCreator.writeValueAsString(gamesWithInvalidResult));
        } catch (NotSubscribingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Responds to an event where a valid result is added to a game.
     * Sends the currently active games to a tournament
     *
     * @param validResultAddedEvent An event where a valid result is added to a game
     */
    @EventListener
    public void onValidResultAdded(ValidResultAddedEvent validResultAddedEvent) {
        int tournamentId = validResultAddedEvent.getTournamentId();
        Collection<? extends Game> games = tournamentsGameService.getActiveGames(tournamentId);
        sendActiveGames(tournamentId, games);
    }

    /**
     * Responds to an event where a result is invalidated.
     * Send the game with invalid result to a tournament
     *
     * @param invalidResultEvent An event where a result is invalidated
     */
    @EventListener
    public void onResultInvalidation(InvalidResultEvent invalidResultEvent) {
        sendGamesWithInvalidResult(invalidResultEvent.getTournamentId(), invalidResultEvent.getGame());
    }

    /**
     * Responds to an event where games is created.
     * Sends active games to a tournament
     *
     * @param gamesCreatedEvent An event where games are created
     */
    @EventListener
    public void onGamesCreation(GamesCreatedEvent gamesCreatedEvent) {
        sendActiveGames(gamesCreatedEvent.getTournamentId(), gamesCreatedEvent.getActiveGames());
    }

    /**
     * Sends active games to a tournament.
     *
     * @param tournamentId The id of the tournament that will receive the active games
     * @param activeGames  The active games to be sent
     */
    private void sendActiveGames(int tournamentId, Collection<? extends Game> activeGames) {
        try {
            messageSender.sendToSubscriber(tournamentId, "/queue/tournament/active-games",
                    jsonCreator.writeValueAsString(activeGames));
        } catch (NotSubscribingException e) {
            e.printStackTrace();
        }
    }
}
