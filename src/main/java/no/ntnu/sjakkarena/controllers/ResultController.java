package no.ntnu.sjakkarena.controllers;

import com.google.gson.Gson;
import no.ntnu.sjakkarena.Session;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.repositories.GameRepository;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class ResultController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Add a result
     *
     * @param opponent The opponent of the player adding the result
     * @param result   The result to be added
     * @return 200 OK if successfully added
     */
    @RequestMapping(value = "/player/add-result", method = RequestMethod.PUT)
    public ResponseEntity<String> addResult(@RequestParam(value = "opponent") int opponent,
                                            @RequestParam(value = "result") String result) {
        if (!Validator.resultIsValid(result)) {
            return new ResponseEntity<>("Not a valid result", HttpStatus.BAD_REQUEST);
        }
        Game game = gameRepository.getActiveGame(Session.getUserId(), opponent); // Has requesting user white pieces?
        if (game == null) {
            game = gameRepository.getActiveGame(opponent, Session.getUserId()); // Has requesting user black pieces?
        }
        if (game == null) {
            return new ResponseEntity<>("Player has no active games", HttpStatus.BAD_REQUEST); // Requesting user has no active games
        }
        gameRepository.addResult(game.getGameId(), result);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Returns the leaderboard of the "signed in" tournament
     *
     * @return the leaderboard of the "signed in" tournament
     */
    @RequestMapping(value = "/tournament/leaderboard", method = RequestMethod.GET)
    public ResponseEntity<String> getTournamentLeaderBoard() {
        int tournamentId = Session.getUserId();
        Collection<Player> players = playerRepository.getPlayersInTournamentSortedByPoints(tournamentId);
        Gson gson = new Gson();
        return new ResponseEntity<>(gson.toJson(players), HttpStatus.OK);
    }


}
