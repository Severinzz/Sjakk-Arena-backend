package no.ntnu.sjakkarena.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Collection;


// TODO change name
@Controller
public class WebSocketController {

    @Autowired
    private TournamentRepository tournamentRepository;

    /**
     * Returns all the names of the players in the tournament
     *
     * @return A json with the player ids mapped to their names
     */
    @MessageMapping("/tournament/players")
    @SendTo("/topic/tournament/players")
    public String getPlayers() {
        int tournamentId = 535937; // TODO
        Collection<Player> players = tournamentRepository.getPlayers(tournamentId);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(players);
    }
}
