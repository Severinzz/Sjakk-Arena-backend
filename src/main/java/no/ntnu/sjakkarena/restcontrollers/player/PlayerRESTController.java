package no.ntnu.sjakkarena.restcontrollers.player;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.services.player.PlayerService;
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

    private JSONCreator jsonCreator = new JSONCreator();

    /**
     * Set a player with a given ID to paused
     *
     * @return 200 OK if successfully set active field to 0, otherwise 400
     */
    @RequestMapping(value = "/pause", method = RequestMethod.PATCH)
    public ResponseEntity<String> pause() {
        try {
            playerService.pausePlayer(RESTSession.getUserId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TroubleUpdatingDBException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Return information about the requesting user
     *
     * @return information relevant to the client application about the requesting user
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getPlayer() {
        try {
            Player player = playerService.getPlayer(RESTSession.getUserId());
            return new ResponseEntity<>(jsonCreator.filterPlayerInformationAndReturnAsJson(player), HttpStatus.OK);
        } catch(NotInDatabaseException e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Unpause the "signed-in" player
     *
     * @return 200 OK if successfully unpaused, otherwise 400
     */
    @RequestMapping(value = "/unpause", method = RequestMethod.PATCH)
    public ResponseEntity<String> unpause() {
        try {
            playerService.unpausePlayer(RESTSession.getUserId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TroubleUpdatingDBException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Set a player with a given ID to inactive
     *
     * @return 200 OK if successfully set active field to 0, otherwise 400
     */
    @RequestMapping(value = "/inactivate", method = RequestMethod.PATCH)
    public ResponseEntity<String> setInactive() {
        try {
            playerService.leaveTournament(RESTSession.getUserId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TroubleUpdatingDBException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes the player with the given JWT
     *
     * @return 200 OK if the player is successfully deleted, otherwise 400
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deletePlayer() {
        try {
            playerService.deletePlayer(RESTSession.getUserId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TroubleUpdatingDBException | NotInDatabaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
