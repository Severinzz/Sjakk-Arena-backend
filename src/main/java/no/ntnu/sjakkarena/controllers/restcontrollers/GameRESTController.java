package no.ntnu.sjakkarena.controllers.restcontrollers;

import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.services.GameService;
import no.ntnu.sjakkarena.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameRESTController {

    @Autowired
    private GameService gameService;

    /**
     * Add a result
     *
     * @param opponentId The opponent of the player adding the result
     * @param whitePlayerPoints     The result to be added
     * @return 200 OK if successfully added. 400 bad if input is not active or if player doesn't have any active games.
     */

    @RequestMapping(value = "player/add-result", method = RequestMethod.PUT)
    public ResponseEntity<String> addResult(@RequestParam(value = "opponent") int opponentId,
                                            @RequestParam(value = "result") double whitePlayerPoints) {
        //TODO change to bean validation + use object called ResultUpdateRequest or equivalent instead of opponent and
        // TODO result variables
        if (!Validator.pointsIsValid(whitePlayerPoints)) {
            return new ResponseEntity<>("Not a valid result", HttpStatus.BAD_REQUEST);
        }
        try {
            gameService.addResult(opponentId, whitePlayerPoints);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotInDatabaseException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
