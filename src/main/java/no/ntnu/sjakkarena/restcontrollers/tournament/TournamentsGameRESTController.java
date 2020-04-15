package no.ntnu.sjakkarena.restcontrollers.tournament;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.services.tournament.TournamentsGameService;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/tournament/games")
public class TournamentsGameRESTController {


    @Autowired
    private TournamentsGameService tournamentsGameService;

    private JSONCreator jsonCreator = new JSONCreator();

    @RequestMapping(value = "/result/{gameID}/{whitePlayerPoints}/", method = RequestMethod.PATCH)
    public ResponseEntity<String> changeGameResult(@PathVariable("gameID") int gameId,
                                                   @PathVariable("whitePlayerPoints") double whitePlayerPoints) {
        try {
            tournamentsGameService.changeGameResult(RESTSession.getUserId(), gameId, whitePlayerPoints);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(IllegalArgumentException | TroubleUpdatingDBException e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Returns the games of the requesting tournament
     *
     * @return the games of the requesting tournament
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getGames() {
        Collection<? extends Game> games = tournamentsGameService.getGames(RESTSession.getUserId());
        return new ResponseEntity<>(jsonCreator.writeValueAsString(games), HttpStatus.OK);
    }
}
