package no.ntnu.sjakkarena.subscriberhandler;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.GamesCreatedEvent;
import no.ntnu.sjakkarena.events.PlayerListChangeEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

public class TournamentSubscriberHandler extends SubscriberHandler {

    private JSONCreator jsonCreator = new JSONCreator();

    /**
     * Sends all the names of the players in the tournament
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
    private void sendLeaderBoard(int tournamentId, List<Player> leaderBoard) {
        try {
            sendToSubscriber(tournamentId, "/queue/tournament/leaderboard",
                    jsonCreator.writeValueAsString(leaderBoard));
        } catch (NullPointerException e) {
            printNotSubscribingErrorMessage("leader board", e);
        }
    }

    private void sendPlayerList(int tournamentId, List<Player> players) {
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
}
