package no.ntnu.sjakkarena.controllers.player;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.tournamentevents.TournamentEndedEvent;
import no.ntnu.sjakkarena.events.tournamentevents.TournamentStartedEvent;
import no.ntnu.sjakkarena.exceptions.NotSubscribingException;
import no.ntnu.sjakkarena.services.player.PlayersTournamentService;
import no.ntnu.sjakkarena.MessageSender;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

/**
 * This class handles WebSocket communication regarding information about players' tournament
 */
@Controller
public class PlayersTournamentController {

    @Autowired
    private PlayersTournamentService playersTournamentService;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private JSONCreator jsonCreator;

    /**
     * Sends information about the active state of a tournament the player sending a message to the
     * "/player/tournament-active" destination. The active state is boolean.
     *
     * @param authentication Authentication of the requesting user
     */
    @MessageMapping("/player/tournament-active")
    public void isTournamentActive(Authentication authentication) {
        int playerId = WebSocketSession.getUserId(authentication);
        informPlayerAboutTournamentState(playerId,
                playersTournamentService.isTournamentActive(playerId));
    }

    /**
     * Responds to an event where a tournament has started.
     * Notifies players about the start of the tournament.
     *
     * @param tournamentStartedEvent An event where a tournament has started.
     */
    @EventListener
    public void onTournamentStart(TournamentStartedEvent tournamentStartedEvent) {
        for (Player player : tournamentStartedEvent.getPlayers()) {
            informPlayerAboutTournamentState(player.getId(), true);
        }
    }

    /**
     * Responds to an event where a tournament has ended.
     * Notifies players about the end of the tournament.
     *
     * @param tournamentEndedEvent An event where a tournament has ended.
     */
    @EventListener
    public void onTournamentEnd(TournamentEndedEvent tournamentEndedEvent) {
        for (Player player : tournamentEndedEvent.getPlayers()) {
            informPlayerAboutTournamentState(player.getId(), false);
        }
    }

    /**
     * Informs the players about the active state of a tournament.
     *
     * @param playerId The player to be informed
     * @param active Whether the tournament is active
     */
    private void informPlayerAboutTournamentState(int playerId, boolean active) {
        try {
            messageSender.sendToSubscriber(playerId, "/queue/player/tournament-active",
                    jsonCreator.createResponseToTournamentStateSubscriber(active));
        } catch (NotSubscribingException e) {
            e.printStackTrace();
        }
    }
}
