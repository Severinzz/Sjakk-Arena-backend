package no.ntnu.sjakkarena.restcontrollers;

import no.ntnu.sjakkarena.data.ResultUpdateRequest;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.services.GameService;
import no.ntnu.sjakkarena.services.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class GameRESTController {

    @Autowired
    private TournamentService tournamentService;

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
    public ResponseEntity<String> invalidResult() {
        String responseMessage = tournamentService.getInvalidGamesWithPlayerNames();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @RequestMapping(value = "tournament/changeResult/{gameID}/{whitePlayerPoints}", method = RequestMethod.PATCH)
    public ResponseEntity<String> changeGameResult(@PathVariable("gameID") String gameID,
                                                   @PathVariable("whitePlayerPoints") String whitePlayerPoints) {
        // TODO: do all this properly. kommenterte linjer gir nullpointerexception, tror jeg har glemt hvordan man gjør dette
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
//            String end = LocalDateTime.now().toString();
//            jdbcTemplate.update("UPDATE `sjakkarena`.`game` SET `white_player_points` = \"" + whiteScore + "\"," +
//                    " `active` = 0, `end` = \"" + end + "\" WHERE game_id = " + gameNR);
//            String sql = "UPDATE sjakkarena.game SET valid_result = 1 WHERE game_id = " + gameNR;
//            jdbcTemplate.update(sql);
            gameService.updateGameResult(gameNR, whiteScore);
            gameService.setGameResultValid(gameNR);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotAbleToUpdateDBException | NotInDatabaseException | NullPointerException e) {
            String message = "gameNR: " + gameNR + " whiteScore: " + whiteScore + " " + e.getMessage();
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }
}
