package no.ntnu.sjakkarena.restcontrollers.tournament;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.services.tournament.TournamentsPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/tournament/player")
public class TournamentsPlayerRESTController {

    @Autowired
    private TournamentsPlayerService tournamentsPlayerService;

    private JSONCreator jsonCreator = new JSONCreator();

    /**
     * Deletes the player with the given ID
     *
     * @return 200 OK if successfully deleted, otherwise 400 BAD REQUEST
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
     * Deletes the player with the given ID
     *
     * @return 200 OK if successfully deleted, otherwise 400 BAD REQUEST
     */
    @RequestMapping(value = "/inactivate/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<String> setPlayerInactive(@PathVariable(name = "id") int playerId,
                                                    @RequestBody String msg) {
        String message = msg.equals("blank") ? "" : msg ;
        try {
            tournamentsPlayerService.inactivatePlayer(playerId, message);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TroubleUpdatingDBException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Returns information about a specific player
     * -or error message if playerID and tournamentID does not exist
     * @return Information about a specific player or error message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getPlayer(@PathVariable(name = "id") int playerId){
        try {
            tournamentsPlayerService.playerBelongsInTournament(playerId);
            Player player = tournamentsPlayerService.getPlayer(playerId);
            return new ResponseEntity<>(jsonCreator.writeValueAsString(player), HttpStatus.OK);
        } catch (NotInDatabaseException | NoSuchElementException e){
            e.printStackTrace();
            if (e instanceof NoSuchElementException) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
