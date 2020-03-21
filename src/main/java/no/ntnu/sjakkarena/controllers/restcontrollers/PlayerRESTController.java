package no.ntnu.sjakkarena.controllers.restcontrollers;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Handles requests from players
 */
@RestController
@RequestMapping("/player")
public class PlayerRESTController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    // TODO create player service and move most logic to the service class

    /**
     * Set a player with a given ID to paused
     *
     * @return 200 OK if successfully set active field to 0, otherwise 400
     */
    @RequestMapping(value = "/pause", method = RequestMethod.PATCH)
    public ResponseEntity<String> pause() {
        try {
            int id = RESTSession.getUserId();
            playerRepository.pausePlayer(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotAbleToUpdateDBException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Return information about the requesting player's tournament
     *
     * @return information about the requesting player's tournament
     */
    @RequestMapping(value = "/tournament", method = RequestMethod.GET)
    public ResponseEntity<String> getTournament() {
        int playerId = RESTSession.getUserId();
        int tournamentId = playerRepository.getPlayer(playerId).getTournamentId();
        Tournament tournament = tournamentRepository.getTournament(tournamentId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", tournament.getTournamentName());
        jsonObject.put("started", tournament.isActive());
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
        int playerId = RESTSession.getUserId();
        Player player = playerRepository.getPlayer(playerId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", player.getName());
        jsonObject.put("points", player.getPoints());
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    /**
     * Unpause the "signed-in" player
     *
     * @return 200 OK if successfully unpaused, otherwise 400
     */
    @RequestMapping(value = "/unpause", method = RequestMethod.PATCH)
    public ResponseEntity<String> setPlayerActive() {
        try {
            playerRepository.unpausePlayer(RESTSession.getUserId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotAbleToUpdateDBException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Set a player with a given ID to inactive
     *
     * @return 200 OK if successfully set active field to 0, otherwise 400
     */
    @RequestMapping(value = "/leave-tournament", method = RequestMethod.PATCH)
    public ResponseEntity<String> leaveTournament() {
        try {
            playerRepository.leaveTournament(RESTSession.getUserId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotAbleToUpdateDBException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes the player with the given JWT
     *
     * @return 200 OK if the player is successfully deleted, otherwise 400
     */
    @RequestMapping(value = "/delete-player", method = RequestMethod.PATCH)
    public ResponseEntity<String> deletePlayer() {
        try {
            playerRepository.deletePlayer(RESTSession.getUserId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotAbleToUpdateDBException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
