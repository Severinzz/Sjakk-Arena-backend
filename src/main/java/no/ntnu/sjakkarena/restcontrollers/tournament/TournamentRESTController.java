package no.ntnu.sjakkarena.restcontrollers.tournament;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.services.tournament.TournamentService;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Handles requests from tournaments regarding information about tournaments
 */
@RestController
@RequestMapping("/tournament")
public class TournamentRESTController {

    @Autowired
    private TournamentService tournamentService;

    private JSONCreator jsonCreator = new JSONCreator();


    /**
     * Returns information about the requesting tournament
     *
     * @return information about the requesting tournament + 200 OK. 400 BAD REQUEST if requesting tournament isn't
     * registered in the database
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getTournament() {
        try {
            Tournament tournament = tournamentService.getTournament(RESTSession.getUserId());
            return new ResponseEntity<>(jsonCreator.writeValueAsString(tournament), HttpStatus.OK);
        } catch (NotInDatabaseException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Starts the requesting tournament
     *
     * @return 200 OK if successfully started. 400 BAD REQUEST if some problems occurred while trying to update the database
     */
    @RequestMapping(value = "/start", method = RequestMethod.PATCH)
    public ResponseEntity<String> startTournament() {
        try {
            tournamentService.startTournament(RESTSession.getUserId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TroubleUpdatingDBException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Ends the requesting tournament
     *
     * @return 200 OK if successfully ended. 400 BAD REQUEST if some problems occurred while trying to update the database
     */
    @RequestMapping(value = "/end", method = RequestMethod.PATCH)
    public ResponseEntity<String> endTournament() {
        try {
            tournamentService.endTournament(RESTSession.getUserId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TroubleUpdatingDBException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Pauses the requesting tournament
     *
     * @return 200 OK if successfully paused. 400 BAD REQUEST if some problems occurred while trying to update the database
     */
    @RequestMapping(value = "/pause", method = RequestMethod.PATCH)
    public ResponseEntity<String> pauseTournament() {
        try {
            tournamentService.pauseTournament(RESTSession.getUserId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TroubleUpdatingDBException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Unpauses the requesting tournament
     *
     * @return 200 OK if successfully paused. 400 BAD REQUEST if some problems occurred while trying to update the database
     */
    @RequestMapping(value = "/unpause", method = RequestMethod.PATCH)
    public ResponseEntity<String> unpauseTournament() {
        try {
            tournamentService.unpauseTournament(RESTSession.getUserId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TroubleUpdatingDBException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
