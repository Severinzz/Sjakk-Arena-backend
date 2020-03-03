package no.ntnu.sjakkarena.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.ntnu.sjakkarena.Session;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.GameTableElement;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.repositories.GameRepository;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.utils.Validator;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;


@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    /**
     * Set a player with a given ID to inactive
     *
     * @return 200 OK if successfully set active field to 0, otherwise 400
     */
    @RequestMapping(value = "/pause", method = RequestMethod.PATCH)
    public ResponseEntity<String> pause() {
        try {
            int id = Session.getUserId();
            playerRepository.pausePlayer(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotAbleToUpdateDBException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Return information about the requesting user's tournament
     * @return information about the requesting user's tournament
     */
    @RequestMapping(value = "/tournament", method = RequestMethod.GET)
    public ResponseEntity<String> getTournament() {
        int playerId = Session.getUserId();
        int tournamentId = playerRepository.getPlayer(playerId).getTournamentId();
        Tournament tournament = tournamentRepository.getTournament(tournamentId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", tournament.getTournamentName());
        jsonObject.put("start", tournament.getStart());
        jsonObject.put("end", tournament.getEnd());
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    /**
     * Return information about the requesting user
     *
     * @return information relevant to the client application about the requesting user
     */
    @RequestMapping(value = "/information", method = RequestMethod.GET)
    public ResponseEntity<String> getPlayer() {
        int playerId = Session.getUserId();
        Player player = playerRepository.getPlayer(playerId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", player.getName());
        jsonObject.put("points", player.getPoints());
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    /**
     * Add a result
     *
     * @param opponent The opponent of the player adding the result
     * @param result   The result to be added
     * @return 200 OK if successfully added. 400 bad if input is not active or if player doesn't have any active games.
     */
    @RequestMapping(value = "add-result", method = RequestMethod.PUT)
    public ResponseEntity<String> setResult(@RequestParam(value = "opponent") int opponent,
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
     * Returns a list of a player's games
     *
     * @return a list of a player's games
     */
    @RequestMapping(value = "/games", method = RequestMethod.GET)
    public ResponseEntity<String> getGames() {
        int playerId = Session.getUserId();
        Collection<GameTableElement> games = gameRepository.getGamesByPlayer(playerId);
        Gson gson = new GsonBuilder().serializeNulls().create();
        return new ResponseEntity<>(gson.toJson(games), HttpStatus.OK);
    }

    /**
     * Set a player with a given ID to Active
     * @return 200 OK if successfully set active field to 0, otherwise 400
     */
    @RequestMapping(value="/player/set-active", method=RequestMethod.PATCH)
    public ResponseEntity<String> setPlayerActive() {
        try {
            int id = Session.getUserId();
            playerRepository.setPlayerActive(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotAbleToUpdateDBException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
