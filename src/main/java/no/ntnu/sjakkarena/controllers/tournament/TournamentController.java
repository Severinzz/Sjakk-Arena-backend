package no.ntnu.sjakkarena.controllers.tournament;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.MessageSender;
import no.ntnu.sjakkarena.events.tournamentevents.TimeToEndTournamentEvent;
import no.ntnu.sjakkarena.events.tournamentevents.TournamentStartedEvent;
import no.ntnu.sjakkarena.exceptions.NotSubscribingException;
import no.ntnu.sjakkarena.services.tournament.TournamentService;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

/**
 * This class handles WebSocket communication regarding information about tournaments
 */
@Controller
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private JSONCreator jsonCreator;

    /**
     * Sends information about the active state of a tournament to the tournament sending a message to the
     * "/tournament/active" destination
     *
     * @param authentication Authentication of the requesting user
     */
    @MessageMapping("/tournament/active")
    public void isActive(Authentication authentication) {
        int tournamentId = WebSocketSession.getUserId(authentication);
        boolean active = tournamentService.isTournamentActive(tournamentId);
        sendActiveStateToTournament(tournamentId, active);
    }

    /**
     * Responds to an event where a tournament has started.
     * Notifies the tournament host about the start of the tournament
     *
     * @param tournamentStartedEvent An event where a tournament has started
     */
    @EventListener
    public void onTournamentStart(TournamentStartedEvent tournamentStartedEvent) {
        sendActiveStateToTournament(tournamentStartedEvent.getTournamentId(), true);
    }

    /**
     * Responds to an event where it is time to end a tournament.
     * Notifies the tournament host about the possible end of the tournament.
     * <p>
     * Whether the tournament ends or not is decided by the tournament host.
     *
     * @param timeToEndTournamentEvent An event where it is time to end a tournament
     */
    @EventListener
    public void onTimeToEndTournament(TimeToEndTournamentEvent timeToEndTournamentEvent) {
        sendActiveStateToTournament(timeToEndTournamentEvent.getTournamentId(), false);
    }

    /**
     * Sends information about a tournaments active state to the tournament host.
     *
     * @param tournamentId The id of the tournament that will receive the active state
     * @param active       Whether the tournament is active
     */
    private void sendActiveStateToTournament(int tournamentId, boolean active) {
        try {
            messageSender.sendToSubscriber(tournamentId, "/queue/tournament/active",
                    jsonCreator.createResponseToTournamentStateSubscriber(active));
        } catch (NotSubscribingException e) {
            e.printStackTrace();
        }
    }
}
