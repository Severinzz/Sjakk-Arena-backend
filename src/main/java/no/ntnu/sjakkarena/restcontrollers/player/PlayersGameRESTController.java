package no.ntnu.sjakkarena.restcontrollers.player;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.ResultUpdateRequest;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.services.player.PlayersGameService;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Handles requests from players regarding the players' games
 */
@RestController
@RequestMapping("/player/games")
public class PlayersGameRESTController {

    @Autowired
    private PlayersGameService playersGameService;

    private JSONCreator jsonCreator = new JSONCreator();

    /**
     * Returns the requesting player's inactive games
     *
     * @return The requesting player's inactive games
     */
    @RequestMapping(value = "/inactive", method = RequestMethod.GET)
    public ResponseEntity<String> getInactiveGames() {
        List<? extends Game> previousGames = playersGameService.getInactiveGames(RESTSession.getUserId());
        return new ResponseEntity<>(jsonCreator.writeValueAsString(previousGames), HttpStatus.OK);
    }

    /**
     * Add a result
     *
     * @param resultUpdateRequest A request to update a result. The request contains the opponent of the requesting
     *                            player and the new result.
     * @return 200 OK if successfully added. 400 bad if input is not active or if player doesn't have any active games.
     */

    @RequestMapping(value = "/add-result", method = RequestMethod.PUT)
    public ResponseEntity<String> addResult(@Valid ResultUpdateRequest resultUpdateRequest) {
        try {
            playersGameService.addResult(resultUpdateRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotInDatabaseException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>("Not a valid result", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Validates the result associated with the specified game
     *
     * @param gameId The id of the game the result is associated with
     * @return 200 OK if successfully validated. 400 BAD REQUEST if player isn't associated with the game
     */
    @RequestMapping(value = "/{game-id}/validate", method = RequestMethod.PATCH)
    public ResponseEntity<String> validateResult(@PathVariable("game-id") int gameId) {
        try {
            playersGameService.setGameResultValid(gameId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Invalidates the result associated with the specified game
     *
     * @param gameId THe id of the game the result is associated with
     * @return 200 OK if successfully invalidated.
     */
    @RequestMapping(value = "/{game-id}/invalidate", method = RequestMethod.PATCH)
    public ResponseEntity<String> invalidateResult(@PathVariable("game-id") int gameId) {
        playersGameService.invalidateResult(gameId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
