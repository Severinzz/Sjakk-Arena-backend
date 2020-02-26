package no.ntnu.sjakkarena.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.ntnu.sjakkarena.Session;
import no.ntnu.sjakkarena.data.GameTableElement;
import no.ntnu.sjakkarena.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    /**
     * Returns the games of the "signed in" tournament
     *
     * @return the games of the "signed in" tournament
     */
    @RequestMapping(value = "/tournament/games", method = RequestMethod.GET)
    public ResponseEntity<String> getTournamentGames() {
        int tournamentId = Session.getUserId();
        Collection<GameTableElement> games = gameRepository.getGamesByTournament(tournamentId);
        Gson gson = new GsonBuilder().serializeNulls().create();
        return new ResponseEntity<>(gson.toJson(games), HttpStatus.OK);
    }

    /**
     * Returns a list of a player's games
     *
     * @return a list of a player's games
     */
    @RequestMapping(value = "/player/games", method = RequestMethod.GET)
    public ResponseEntity<String> getPlayerGames() {
        int playerId = Session.getUserId();
        Collection<GameTableElement> games = gameRepository.getGamesByPlayer(playerId);
        Gson gson = new GsonBuilder().serializeNulls().create();
        return new ResponseEntity<>(gson.toJson(games), HttpStatus.OK);
    }
}
