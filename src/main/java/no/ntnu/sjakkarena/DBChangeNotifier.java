package no.ntnu.sjakkarena;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Collection;

public class DBChangeNotifier {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * Returns all the names of the players in the tournament
     *
     * @return A json with the player ids mapped to their names
     */
    public void notifyUpdatedPlayerList(int tournamentId) {
        Collection<Player> players = tournamentRepository.getPlayers(tournamentId);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        simpMessagingTemplate.convertAndSend("/topic/tournament/players", gson.toJson(players));
    }
}
