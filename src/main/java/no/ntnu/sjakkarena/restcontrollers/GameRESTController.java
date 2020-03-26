package no.ntnu.sjakkarena.restcontrollers;

import no.ntnu.sjakkarena.data.ResultUpdateRequest;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.repositories.GameRepository;
import no.ntnu.sjakkarena.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class GameRESTController {

    @Autowired
    private GameService gameService;

    /**
     * Add a result
     *
     * @return 200 OK if successfully added. 400 bad if input is not active or if player doesn't have any active games.
     */

    @RequestMapping(value = "player/add-result", method = RequestMethod.PUT)
    public ResponseEntity<String> addResult(@Valid ResultUpdateRequest resultUpdateRequest) {
        try {
            gameService.addResult(resultUpdateRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotInDatabaseException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>("Not a valid result", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "tournament/invalidGames", method = RequestMethod.GET)
    public ResponseEntity<String> invalidResult(@PathVariable(name = "invalidResultGame") int tournamentID) {
        try {
            GameRepository game = new GameRepository();
            return new ResponseEntity(game.getInvalidResultGames(tournamentID), HttpStatus.OK);
        } catch (NotInDatabaseException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
