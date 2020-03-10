package no.ntnu.sjakkarena;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.User;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.utils.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Collection;

public class DBChangeNotifier {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * Sends all the names of the players in the tournament
     *
     * @return A json with the player ids mapped to their names
     */
    public void notifyUpdatedPlayerList(int tournamentId) {
        Collection<Player> players = tournamentRepository.getPlayers(tournamentId);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            User user = UserStorage.getUser(tournamentId);
            simpMessagingTemplate.convertAndSendToUser(user.toString(),
                    "/queue/tournament/players", gson.toJson(players));
        } catch (NullPointerException e) {
            printNotSubscribingErrorMessage("a list of players", e);
        }
    }

    /**
     * Sends the tournaments leaderboard
     *
     * @param tournamentId The id of the tournament
     */
    public void notifyUpdatedLeaderboard(int tournamentId) {
        Collection<Player> players = tournamentRepository.getPlayersSortedByPoints(tournamentId);
        Gson gson = new Gson();
        try {
            User user = UserStorage.getUser(tournamentId);
            simpMessagingTemplate.convertAndSendToUser(user.toString(), "/queue/tournament/leaderboard",
                    gson.toJson(players));
        } catch (NullPointerException e) {
            printNotSubscribingErrorMessage("a leaderboard", e);
        }
    }

    /**
     * Prints an error message when the user to be notified doesn't subscribe to a service
     * @param serviceDescription A description of the service
     * @param exception
     */
    private void printNotSubscribingErrorMessage(String serviceDescription, Exception exception){
        System.out.println("Tournament is probably not subscribing to the service providing + " + serviceDescription
                + "\n" + exception);
    }
}
