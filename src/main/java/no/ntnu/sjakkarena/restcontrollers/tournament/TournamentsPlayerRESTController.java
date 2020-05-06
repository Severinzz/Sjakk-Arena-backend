package no.ntnu.sjakkarena.restcontrollers.tournament;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.services.tournament.TournamentsPlayerService;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

/**
 * Handles requests from tournaments regarding tournament's players
 */
@RestController
@RequestMapping("/tournament/player")
public class TournamentsPlayerRESTController {

    @Autowired
    private TournamentsPlayerService tournamentsPlayerService;

    private JSONCreator jsonCreator = new JSONCreator();

    /**
     * Deletes the player with the given ID
     *
     * @param playerId The id of the player to delete
     * @param msg      The msg sent to the deleted player
     * @return 200 OK if successfully deleted. 400 BAD REQUEST if some problems occurred while trying to update the database
     */
    @RequestMapping(value = "/delete/{playerId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deletePlayer(@PathVariable(name = "playerId") int playerId,
                                               @RequestParam(defaultValue = "") String msg) {
        try {
            tournamentsPlayerService.deletePlayer(playerId, msg);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TroubleUpdatingDBException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Inactivates the player with the given ID
     *
     * @param playerId The id fo the player to inactivate
     * @param msg      The message sent to the inactivated player
     * @return 200 OK if successfully deleted. 400 BAD REQUEST if some problems occurred while trying to update the database
     */
    @RequestMapping(value = "/inactivate/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<String> setPlayerInactive(@PathVariable(name = "id") int playerId,
                                                    @RequestBody String msg) {
        String message = msg.equals("blank") ? "" : msg;
        try {
            tournamentsPlayerService.removePlayerFromTournament(playerId, message);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TroubleUpdatingDBException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Returns information about the specified player
     * -or error message if playerID and tournamentID does not exist
     *
     * @param playerId The id of the player
     * @return Information about a specific player or error message + 200 OK. 400 Bad request if the player is not in
     * the database. 403 FORBIDDEN if player is not enrolled in the tournament
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getPlayer(@PathVariable(name = "id") int playerId) {
        try {
            Player player = tournamentsPlayerService.getPlayer(playerId, RESTSession.getUserId());
            return new ResponseEntity<>(jsonCreator.writeValueAsString(player), HttpStatus.OK);
        } catch (NotInDatabaseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
