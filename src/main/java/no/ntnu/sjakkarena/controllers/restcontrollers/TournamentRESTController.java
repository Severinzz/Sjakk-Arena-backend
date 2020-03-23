package no.ntnu.sjakkarena.controllers.restcontrollers;

import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.services.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles requests from tournaments
 */
@RestController
@RequestMapping("/tournament")
public class TournamentRESTController {

    @Autowired
    private TournamentService tournamentService;

    /**
     * Get information about the requesting tournament
     *
     * @return information about the requesting tournament
     */
    @RequestMapping(value = "/information", method = RequestMethod.GET)
    public ResponseEntity<String> getTournament() {
        try {
            String responseMessage = tournamentService.getTournament();
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (NotInDatabaseException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes the player with the given ID
     *
     * @return 200 OK if successfully deleted, otherwise 400 BAD REQUEST
     */
    @RequestMapping(value = "/delete-player/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deletePlayer(@PathVariable(name = "id") int id) {
        try {
            tournamentService.deletePlayer(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotAbleToUpdateDBException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Returns the games of the requesting tournament
     *
     * @return the games of the requesting tournament
     */
    @RequestMapping(value = "/games", method = RequestMethod.GET)
    public ResponseEntity<String> getGames() {
        String responseMessage = tournamentService.getGamesWithPlayerNames();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    // TODO change to websocket. Tournament subscribes to service where tournament is notified on tournament start
    @RequestMapping(value = "/start", method = RequestMethod.PATCH)
    public ResponseEntity<String> startTournament() {
        try {
            tournamentService.startTournament();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(NotAbleToUpdateDBException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
