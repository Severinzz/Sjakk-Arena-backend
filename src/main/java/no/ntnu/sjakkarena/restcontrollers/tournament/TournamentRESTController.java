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
 * Handles requests from tournaments
 */
@RestController
@RequestMapping("/tournament")
public class TournamentRESTController {

    @Autowired
    private TournamentService tournamentService;

    private JSONCreator jsonCreator = new JSONCreator();


    /**
     * Get information about the requesting tournament
     *
     * @return information about the requesting tournament
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getTournament() {
        try {
            Tournament tournament =  tournamentService.getTournament(RESTSession.getUserId());
            return new ResponseEntity<>(jsonCreator.writeValueAsString(tournament), HttpStatus.OK);
        } catch (NotInDatabaseException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/start", method = RequestMethod.PATCH)
    public ResponseEntity<String> startTournament() {
        try {
            tournamentService.startTournament(RESTSession.getUserId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(TroubleUpdatingDBException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
