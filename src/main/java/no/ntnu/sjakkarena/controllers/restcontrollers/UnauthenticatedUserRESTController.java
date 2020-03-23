package no.ntnu.sjakkarena.controllers.restcontrollers;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.ImproperlyFormedDataException;
import no.ntnu.sjakkarena.exceptions.NameAlreadyExistsException;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.services.UnauthenticatedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Handles requests from users who don't have a token to identify them.  All methods in this class will return a
 * token to the user.
 */
@RestController
public class UnauthenticatedUserRESTController {

    @Autowired
    private UnauthenticatedUserService unauthenticatedUserService;

    /**
     * Register a new user
     *
     * @param player
     * @return
     */
    @RequestMapping(value = "/new-player", method = RequestMethod.POST)
    public ResponseEntity<String> registerPlayer(@RequestBody Player player) {
        try {
            String responseMessage = unauthenticatedUserService.addNewPlayer(player);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (NotAbleToUpdateDBException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NameAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT); // frontend leter etter kode 409
        }
    }

    /**
     * Register a tournament
     *
     * @param tournament tournament
     * @return HTTP status 200 ok if successfully added + jwt and tournamentID,
     * HTTP status 422 UNPROCESSABLE_ENTITY otherwise
     */
    @RequestMapping(value = "/new-tournament", method = RequestMethod.POST)
    public ResponseEntity<String> registerTournament(@Valid @RequestBody Tournament tournament) {
        try {
            String responseMessage = unauthenticatedUserService.handleAddTournamentRequest(tournament);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (NotAbleToUpdateDBException | ImproperlyFormedDataException | NullPointerException e) {
            return new ResponseEntity<>("Couldn't register tournament", HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * "Sign in" using adminUUID.
     *
     * @param adminUUID the universally unique identifier of the tournament
     * @return 200 OK + a jwt with subject: "TOURNAMENT" and tournament id as an id.
     * 400 Bad Request if there is no tournament associated with the UUID.
     */
    @RequestMapping(value = "/sign-in/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<String> signIn(@PathVariable(name = "uuid") String adminUUID) {
        try{
            String responseMessage = unauthenticatedUserService.signIn(adminUUID);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch(NotInDatabaseException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
