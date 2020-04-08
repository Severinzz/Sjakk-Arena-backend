package no.ntnu.sjakkarena.subscriberhandler;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.gameevents.InvalidResultEvent;
import no.ntnu.sjakkarena.events.gameevents.GamesCreatedEvent;
import no.ntnu.sjakkarena.events.playerevents.PlayerListChangeEvent;
import no.ntnu.sjakkarena.events.tournamentevents.TimeToEndTournamentEvent;
import no.ntnu.sjakkarena.events.tournamentevents.TournamentEndedEvent;
import no.ntnu.sjakkarena.events.tournamentevents.TournamentStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class TournamentSubscriberHandler extends SubscriberHandler {

    private JSONCreator jsonCreator = new JSONCreator();

    /**
     * Sends all the names of the players in the tournamentevents
     */
    @EventListener
    public void onPlayerListChange(PlayerListChangeEvent playerListChangeEvent) {
        if (playerListChangeEvent.hasTournamentStarted()) {
            sendLeaderBoard(playerListChangeEvent.getTournamentId(),
                    playerListChangeEvent.getLeaderBoard());
        } else {
            sendPlayerList(playerListChangeEvent.getTournamentId(),
                    playerListChangeEvent.getPlayers());
        }
    }

    /**
     * Sends the tournaments leaderboard
     */
    public void sendLeaderBoard(int tournamentId, List<Player> leaderBoard) {
        try {
            sendToSubscriber(tournamentId, "/queue/tournament/leaderboard",
                    jsonCreator.writeValueAsString(leaderBoard));
        } catch (NullPointerException e) {
            printNotSubscribingErrorMessage("leader board", e);
        }
    }

    public void sendPlayerList(int tournamentId, List<Player> players) {
        try {
            sendToSubscriber(tournamentId, "/queue/tournament/players",
                    jsonCreator.writeValueAsString(players));
        } catch (NullPointerException e) {
            printNotSubscribingErrorMessage("players list", e);
        }
    }

    /**
     * @param gamesCreatedEvent
     */
    @EventListener
    public void onGamesCreation(GamesCreatedEvent gamesCreatedEvent) {
        try {
            sendToSubscriber(gamesCreatedEvent.getTournamentId(), "/queue/tournament/active-games",
                    jsonCreator.writeValueAsString(gamesCreatedEvent.getActiveGames()));
        } catch (NullPointerException e) {
            printNotSubscribingErrorMessage("new games", e);
        }
    }

    public void sendActiveGamesList(int tournamentId, Collection<? extends Game> games){
        try {
            sendToSubscriber(tournamentId, "/queue/tournament/active-games",
                    jsonCreator.writeValueAsString(games));
        } catch (NullPointerException e) {
            printNotSubscribingErrorMessage("active games", e);
        }
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

    public void sendActiveStateToTournament(int tournamentId, boolean active){
        try {
            sendToSubscriber(tournamentId, "/queue/tournament/active",
                    jsonCreator.createResponseToTournamentStateSubscriber(active));
        } catch (NullPointerException e) {
            printNotSubscribingErrorMessage("tournament status", e);
        }
    }

    @EventListener
    public void onResultInvalidation(InvalidResultEvent invalidResultEvent){
        try{
            sendToSubscriber(invalidResultEvent.getTournamentId(), "/queue/tournament/games/invalid",
                    jsonCreator.writeValueAsString(invalidResultEvent.getGame()));
        } catch (NullPointerException e){
            printNotSubscribingErrorMessage("games with invalid result", e);
        }
    }
}
