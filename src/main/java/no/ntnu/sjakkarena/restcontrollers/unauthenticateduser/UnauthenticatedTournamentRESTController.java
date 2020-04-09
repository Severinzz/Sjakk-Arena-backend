package no.ntnu.sjakkarena.restcontrollers.unauthenticateduser;

import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.ImproperlyFormedDataException;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.services.unauthenticateduser.UnauthenticatedTournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UnauthenticatedTournamentRESTController {

    @Autowired
    private UnauthenticatedTournamentService unauthenticatedTournamentService;

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
            String responseMessage = unauthenticatedTournamentService.handleAddTournamentRequest(tournament);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (TroubleUpdatingDBException | ImproperlyFormedDataException | NullPointerException e) {
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
            String responseMessage = unauthenticatedTournamentService.signIn(adminUUID);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch(NotInDatabaseException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
