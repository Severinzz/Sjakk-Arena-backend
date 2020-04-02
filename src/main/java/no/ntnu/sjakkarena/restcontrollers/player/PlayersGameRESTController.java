package no.ntnu.sjakkarena.restcontrollers.player;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.services.player.PlayersGameService;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/player/games")
public class PlayersGameRESTController {

    @Autowired
    private PlayersGameService playersGameService;

    private JSONCreator jsonCreator = new JSONCreator();

    @RequestMapping(value="/inactive", method= RequestMethod.GET)
    public ResponseEntity<String> getInactiveGames(){
        List<? extends Game> previousGames =  playersGameService.getInactiveGames(RESTSession.getUserId());
        return new ResponseEntity<>(jsonCreator.writeValueAsString(previousGames), HttpStatus.OK);
    }


}
