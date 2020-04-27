package no.ntnu.sjakkarena.restcontrollers.player;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.services.player.PlayersTournamentService;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles requests from players regarding the players' tournament
 */
@RestController
@RequestMapping("/player/tournament")
public class PlayersTournamentRESTController {

    private JSONCreator jsonCreator = new JSONCreator();

    @Autowired
    private PlayersTournamentService playersTournamentService;

    /**
     * Returns information about the requesting player's tournament
     *
     * @return information about the requesting player's tournament + 200 OK. 400 BAD REQUEST if the tournament
     * isn't in the database.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getTournament() {
        try {
            Tournament playersTournament = playersTournamentService.getTournament(RESTSession.getUserId());
            return new ResponseEntity<>(jsonCreator.filterPlayersTournamentInformationAndReturnAsJson(playersTournament),
                    HttpStatus.OK);
        } catch (NotInDatabaseException e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Returns true if requesting player's tournament is active
     *
     * @return true if requesting player's tournament is active + 200 OK. 400 BAD REQUEST if the tournament isn't in
     * the database.
     */
    @RequestMapping(value="/active", method = RequestMethod.GET)
    public ResponseEntity<String> isTournamentActive() {
        try {
            boolean active = playersTournamentService.isTournamentActive(RESTSession.getUserId());
            return new ResponseEntity<>(jsonCreator.createResponseToTournamentStateSubscriber(active), HttpStatus.OK);
        } catch (NotInDatabaseException e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
