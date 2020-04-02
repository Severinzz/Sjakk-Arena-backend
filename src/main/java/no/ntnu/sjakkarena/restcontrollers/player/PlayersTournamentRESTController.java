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

@RestController
@RequestMapping("/player/tournament")
public class PlayersTournamentRESTController {

    private JSONCreator jsonCreator = new JSONCreator();

    @Autowired
    private PlayersTournamentService playersTournamentService;

    /**
     * Return information about the requesting player's tournament
     *
     * @return information about the requesting player's tournament
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
