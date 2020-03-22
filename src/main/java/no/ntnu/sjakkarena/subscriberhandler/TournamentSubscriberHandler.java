package no.ntnu.sjakkarena.subscriberhandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.NewGamesEvent;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.util.Collection;

public class TournamentSubscriberHandler extends SubscriberHandler{

    @Autowired
    private TournamentRepository tournamentRepository;


    /**
     * Sends all the names of the players in the tournament
     *
     * @return A json with the player ids mapped to their names
     */
    public void sendPlayerList(int tournamentId) {
        try {
            // TODO remove tournament repository.getPl... use events instead
            Collection<Player> players = tournamentRepository.getPlayers(tournamentId);
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            sendToSubscriber(tournamentId,"/queue/tournament/players", gson.toJson(players));
        } catch (NullPointerException e) {
            printNotSubscribingErrorMessage("a list of players", e);
        }
    }

    /**
     * Sends the tournaments leaderboard
     *
     * @param tournamentId The id of the tournament
     */
    public void sendLeaderBoard(int tournamentId) {
        try {
            // TODO remove tournament repository.getPl... use events instead
            Collection<Player> players = tournamentRepository.getPlayersSortedByPoints(tournamentId);
            Gson gson = new Gson();
            sendToSubscriber(tournamentId, "/queue/tournament/leaderboard", gson.toJson(players));
        } catch (NullPointerException e) {
            printNotSubscribingErrorMessage("a leaderboard", e);
        }
    }

    /**
     *
     * @param newGamesEvent
     */
    @EventListener
    public void handleNewGamesEvent(NewGamesEvent newGamesEvent) {
        try {
            Gson gson = new Gson();
            sendToSubscriber(newGamesEvent.getTournamentId(), "/queue/tournament/games",
                    gson.toJson(newGamesEvent.getGames()));
        }
        catch(NullPointerException e){
            printNotSubscribingErrorMessage("new games", e);
        }
    }
}
