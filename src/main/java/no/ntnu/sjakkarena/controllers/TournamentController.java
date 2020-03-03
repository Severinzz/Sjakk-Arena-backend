package no.ntnu.sjakkarena.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.ntnu.sjakkarena.Session;
import no.ntnu.sjakkarena.data.GameTableElement;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.repositories.GameRepository;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/tournament")
public class TournamentController {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    /**
     * Get information about the requesting tournament
     *
     * @return information about the requesting tournament
     */
    @RequestMapping(value = "/information", method = RequestMethod.GET)
    public ResponseEntity<String> getTournament() {
        int tournamentId = Session.getUserId();
        Tournament tournament = tournamentRepository.getTournament(tournamentId);
        Gson gson = new Gson();
        return new ResponseEntity<>(gson.toJson(tournament), HttpStatus.OK);
    }

    /**
     * Deletes the player with the given ID
     *
     * @return 200 OK if successfully deleted, otherwise 400 BAD REQUEST
     */
    @RequestMapping(value = "/delete-player/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deletePlayer(@PathVariable(name = "id") int id) {
        try {
            playerRepository.deletePlayer(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotAbleToUpdateDBException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Returns all the names of the players in the tournament
     *
     * @return A json with the player ids mapped to their names
     */
    @RequestMapping(value = "/players", method = RequestMethod.GET)
    public ResponseEntity<String> getPlayers() {
        int tournamentId = Session.getUserId();
        Collection<Player> players = playerRepository.getPlayers(tournamentId);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return new ResponseEntity<>(gson.toJson(players), HttpStatus.OK);
    }

    /**
     * Returns the leaderboard of the requesting tournament
     *
     * @return the leaderboard of the requesting tournament
     */
    @RequestMapping(value = "/leaderboard", method = RequestMethod.GET)
    public ResponseEntity<String> getLeaderboard() {
        int tournamentId = Session.getUserId();
        Collection<Player> players = playerRepository.getPlayersInTournamentSortedByPoints(tournamentId);
        Gson gson = new Gson();
        return new ResponseEntity<>(gson.toJson(players), HttpStatus.OK);
    }

    /**
     * Returns the games of the requesting tournament
     *
     * @return the games of the requesting tournament
     */
    @RequestMapping(value = "/games", method = RequestMethod.GET)
    public ResponseEntity<String> getGames() {
        int tournamentId = Session.getUserId();
        Collection<GameTableElement> games = gameRepository.getGamesByTournament(tournamentId);
        Gson gson = new GsonBuilder().serializeNulls().create();
        return new ResponseEntity<>(gson.toJson(games), HttpStatus.OK);
    }
}
