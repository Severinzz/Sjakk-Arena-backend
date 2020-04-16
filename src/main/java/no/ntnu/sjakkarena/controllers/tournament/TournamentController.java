package no.ntnu.sjakkarena.controllers.tournament;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.events.tournamentevents.TimeToEndTournamentEvent;
import no.ntnu.sjakkarena.events.tournamentevents.TournamentEndedEvent;
import no.ntnu.sjakkarena.events.tournamentevents.TournamentStartedEvent;
import no.ntnu.sjakkarena.exceptions.NotSubscribingException;
import no.ntnu.sjakkarena.services.tournament.TournamentService;
import no.ntnu.sjakkarena.MessageSender;
import no.ntnu.sjakkarena.utils.WebSocketSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private JSONCreator jsonCreator;

    @MessageMapping("/tournament/active")
    public void isActive(Authentication authentication){
        int tournamentId = WebSocketSession.getUserId(authentication);
        boolean active = tournamentService.isTournamentActive(tournamentId);
        sendActiveStateToTournament(tournamentId, active);
    }

    @EventListener
    public void onTournamentStart(TournamentStartedEvent tournamentStartedEvent){
        sendActiveStateToTournament(tournamentStartedEvent.getTournamentId(), true);
    }

    @EventListener
    public void onTimeToEndTournament(TimeToEndTournamentEvent timeToEndTournamentEvent){
        sendActiveStateToTournament(timeToEndTournamentEvent.getTournamentId(), false);
    }

    @EventListener
    public void onTournamentEnd(TournamentEndedEvent tournamentEndedEvent){
        sendActiveStateToTournament(tournamentEndedEvent.getTournamentId(), false);
    }

    private void sendActiveStateToTournament(int tournamentId, boolean active){
        try {
            messageSender.sendToSubscriber(tournamentId, "/queue/tournament/active",
                    jsonCreator.createResponseToTournamentStateSubscriber(active));
        } catch (NotSubscribingException e) {
            e.printStackTrace();
        }
    }
}
