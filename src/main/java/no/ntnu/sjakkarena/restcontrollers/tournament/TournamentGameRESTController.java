package no.ntnu.sjakkarena.restcontrollers.tournament;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.services.tournament.TournamentGameService;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tournament/games")
public class TournamentGameRESTController {


    @Autowired
    private TournamentGameService tournamentGameService;

    private JSONCreator jsonCreator = new JSONCreator();

    @RequestMapping(value = "/invalid", method = RequestMethod.GET)
    public ResponseEntity<String> invalidResult() {
        List<? extends Game> invalidGame = tournamentGameService.getGamesWithInvalidResult(RESTSession.getUserId());
        return new ResponseEntity<>(jsonCreator.writeValueAsString(invalidGame), HttpStatus.OK);
    }

    @RequestMapping(value = "/{gameID}/{whitePlayerPoints}/", method = RequestMethod.PATCH)
    public ResponseEntity<String> changeGameResult(@PathVariable("gameID") int gameId,
                                                   @PathVariable("whitePlayerPoints") double whitePlayerPoints) {
        try {
            tournamentGameService.changeGameResult(gameId, whitePlayerPoints);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(IllegalArgumentException | TroubleUpdatingDBException e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
