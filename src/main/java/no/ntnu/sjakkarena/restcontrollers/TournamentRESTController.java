package no.ntnu.sjakkarena.restcontrollers;

import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.services.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Handles requests from tournaments
 */
@RestController
@RequestMapping("/tournament")
public class TournamentRESTController {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
     * Deletes the player with the given ID
     *
     * @return 200 OK if successfully deleted, otherwise 400 BAD REQUEST
     */
    @RequestMapping(value = "/set-player-inactive/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<String> setPlayerInactive(@PathVariable(name = "id") int playerId) {
        try {
            tournamentService.inactivatePlayer(playerId);
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

    @RequestMapping(value = "/start", method = RequestMethod.PATCH)
    public ResponseEntity<String> startTournament() {
        try {
            tournamentService.startTournament();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(NotAbleToUpdateDBException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Returns information about a specific player
     * @return Information about a specific player
     */
    @RequestMapping(value = "/player/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getPlayer(@PathVariable(name = "id") int playerId){
        String player = tournamentService.getPlayer(playerId);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }


    @RequestMapping(value = "/invalidGames", method = RequestMethod.GET)
    public ResponseEntity<String> invalidResult() {
        String responseMessage = tournamentService.getInvalidGamesWithPlayerNames();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @RequestMapping(value = "/changeResult/{gameID}/{whitePlayerPoints}", method = RequestMethod.PATCH)
    public ResponseEntity<String> changeGameResult(@PathVariable("gameID") String gameID,
                                                   @PathVariable("whitePlayerPoints") String whitePlayerPoints) {
        // TODO: do all this properly
        int gameNR = Integer.parseInt(gameID);
        double whiteScore;
        if (whitePlayerPoints.equals("0,5")) { // "0,5" cannot be parsed, "0.5" cannot be in URL
            whiteScore = 0.5;
        } else if (whitePlayerPoints.equals("1")) {
            whiteScore = 1.0;
        } else if (whitePlayerPoints.equals("0")) {
            whiteScore = 0.0;
        } else { // Do not accept any other values.
            String message = "Score cannot be: " + whitePlayerPoints;
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        try {
            String end = LocalDateTime.now().toString();
            jdbcTemplate.update("UPDATE `sjakkarena`.`game` SET `white_player_points` = \"" + whiteScore + "\"," +
                    " `active` = 0, `end` = \"" + end + "\" WHERE game_id = " + gameNR);
            String sql = "UPDATE sjakkarena.game SET valid_result = 1 WHERE game_id = " + gameNR;
            jdbcTemplate.update(sql);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotAbleToUpdateDBException | NotInDatabaseException | NullPointerException e) {
            String message = "gameNR: " + gameNR + " whiteScore: " + whiteScore + " " + e.getMessage();
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

    }
}