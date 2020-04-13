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

@Controller
public class PlayersTournamentController {

    @Autowired
    private PlayersTournamentService playersTournamentService;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private JSONCreator jsonCreator;

    @MessageMapping("/player/tournament-active")
    public void isTournamentActive(Authentication authentication){
        int playerId = WebSocketSession.getUserId(authentication);
        informPlayerAboutTournamentState(playerId,
                playersTournamentService.isTournamentActive(playerId));
    }

    @EventListener
    public void onTournamentStart(TournamentStartedEvent tournamentStartedEvent){
        for (Player player : tournamentStartedEvent.getPlayers()){
            informPlayerAboutTournamentState(player.getId(), true);
        }

    }

    @EventListener
    public void onTournamentEnd(TournamentEndedEvent tournamentEndedEvent){
        for (Player player: tournamentEndedEvent.getPlayers()){
            informPlayerAboutTournamentState(player.getId(), false);
        }
    }

    private void informPlayerAboutTournamentState(int playerId, boolean active) {
        try{
            messageSender.sendToSubscriber(playerId, "/queue/player/tournament-active",
                    jsonCreator.createResponseToTournamentStateSubscriber(active));
        } catch(NotSubscribingException e){
            e.printStackTrace();
        }
    }
}
