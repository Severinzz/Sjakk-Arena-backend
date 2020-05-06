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
 * Handles requests from players regarding information about players
 */
@RestController
@RequestMapping("/player")
public class PlayerRESTController {

    @Autowired
    private PlayerService playerService;

    private JSONCreator jsonCreator = new JSONCreator();

    /**
     * Pauses the requesting player
     *
     * @return 200 OK if successfully paused. 400 BAD REQUEST if some problems occurred while trying to update the database
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
     * Returns information about the requesting player
     *
     * @return Information about the requesting user + 200 OK. 400 BAD REQUEST if player isn't in the database
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getPlayer() {
        try {
            Player player = playerService.getPlayer(RESTSession.getUserId());
            return new ResponseEntity<>(jsonCreator.filterPlayerInformationAndReturnAsJson(player), HttpStatus.OK);
        } catch (NotInDatabaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Unpauses the requesting player
     *
     * @return 200 OK if successfully unpaused. 400 BAD REQUEST if some problems occurred while trying to update
     * the database
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
     * Inactivates the requesting player. If a player is inactivated, he/her is no longer actively playing in
     * the tournament
     *
     * @return 200 OK if successfully set active field to 0. 400 BAD REQUEST if some problems occurred while trying to
     * update the database
     */
    @RequestMapping(value = "/inactivate", method = RequestMethod.PATCH)
    public ResponseEntity<String> leaveTournament() {
        try {
            playerService.leaveTournament(RESTSession.getUserId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TroubleUpdatingDBException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes the requesting player.
     *
     * @return 200 OK if the player is successfully deleted. 400 BAD REQUEST if some problems occurred while trying to
     * update the database or if the player isn't in the database
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
