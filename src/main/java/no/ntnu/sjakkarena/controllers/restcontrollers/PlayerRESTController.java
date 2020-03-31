package no.ntnu.sjakkarena.controllers.restcontrollers;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.services.PlayerService;
import no.ntnu.sjakkarena.utils.RESTSession;
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
    private PlayerService playerService;

    JSONCreator jsonCreator = new JSONCreator();

    /**
     * Set a player with a given ID to paused
     *
     * @return 200 OK if successfully set active field to 0, otherwise 400
     */
    @RequestMapping(value = "/pause", method = RequestMethod.PATCH)
    public ResponseEntity<String> pause() {
        try {
            playerService.pausePlayer();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotAbleToUpdateDBException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Return information about the requesting player's tournament
     *
     * @return information about the requesting player's tournament
     */
    @RequestMapping(value = "/tournament", method = RequestMethod.GET)
    public ResponseEntity<String> getTournament() {
        try {
            Tournament playersTournament = playerService.getPlayersTournament();
            return new ResponseEntity<>(jsonCreator.filterPlayersTournamentInformationAndReturnAsJson(playersTournament),
                    HttpStatus.OK);
        } catch (NotInDatabaseException e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Return information about the requesting user
     *
     * @return information relevant to the client application about the requesting user
     */
    @RequestMapping(value = "/information", method = RequestMethod.GET)
    public ResponseEntity<String> getPlayer() {
        try {
            Player player = playerService.getPlayer(RESTSession.getUserId());
            return new ResponseEntity<>(jsonCreator.filterPlayerInformationAndReturnAsJson(player), HttpStatus.OK);
        } catch(NotInDatabaseException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Unpause the "signed-in" player
     *
     * @return 200 OK if successfully unpaused, otherwise 400
     */
    @RequestMapping(value = "/unpause", method = RequestMethod.PATCH)
    public ResponseEntity<String> setPlayerActive() {
        try {
            playerService.unpausePlayer();
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
    @RequestMapping(value = "/set-inactive", method = RequestMethod.PATCH)
    public ResponseEntity<String> setInactive() {
        try {
            playerService.setInactive();
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
    @RequestMapping(value = "/delete-player", method = RequestMethod.DELETE)
    public ResponseEntity<String> deletePlayer() {
        try {
            playerService.deletePlayer();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotAbleToUpdateDBException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/tournament-status", method = RequestMethod.GET)
    public ResponseEntity<String> isTournamentActive() {
        try {
            boolean active = playerService.isTournamentActive(RESTSession.getUserId());
            return new ResponseEntity<>(jsonCreator.createResponseToTournamentStateSubscriber(active), HttpStatus.OK);
        } catch (NotInDatabaseException e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
