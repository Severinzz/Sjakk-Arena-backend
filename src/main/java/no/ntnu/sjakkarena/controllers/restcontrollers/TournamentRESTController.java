package no.ntnu.sjakkarena.controllers.restcontrollers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.ntnu.sjakkarena.data.GameWithPlayerNames;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.repositories.GameWithPlayerNamesRepository;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.services.TournamentService;
import no.ntnu.sjakkarena.subscriberhandler.TournamentSubscriberHandler;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Handles requests from tournaments
 */
@RestController
@RequestMapping("/tournament")
public class TournamentRESTController {

    // TODO move repositories to service class

    @Autowired
    private TournamentSubscriberHandler tournamentSubscriberHandler;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private GameWithPlayerNamesRepository gameWithPlayerNamesRepository;

    /**
     * Get information about the requesting tournament
     *
     * @return information about the requesting tournament
     */
    @RequestMapping(value = "/information", method = RequestMethod.GET)
    public ResponseEntity<String> getTournament() {
        int tournamentId = RESTSession.getUserId();
        Tournament tournament = tournamentRepository.getTournament(tournamentId);
        Gson gson = new Gson();
        return new ResponseEntity<>(gson.toJson(tournament), HttpStatus.OK);
    }

    /**
     * Deletes the player with the given ID
     *
     * @return 200 OK if successfully deleted, otherwise 400 BAD REQUEST
     */
    @RequestMapping(value = "/delete-player/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deletePlayer(@PathVariable(name = "id") int id) {
        try {
            playerRepository.deletePlayer(id);
            // TODO change to event handling
            tournamentSubscriberHandler.sendPlayerList(RESTSession.getUserId());
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
        int tournamentId = RESTSession.getUserId();
        Collection<GameWithPlayerNames> games = gameWithPlayerNamesRepository.getGamesWithPlayerNames(tournamentId);
        Gson gson = new GsonBuilder().serializeNulls().create();
        return new ResponseEntity<>(gson.toJson(games), HttpStatus.OK);
    }

    // TODO change to websocket. Tournament subscribes to service where tournament is notified on tournament start
    @RequestMapping(value = "/start", method = RequestMethod.PATCH)
    public ResponseEntity<String> startTournament(){
        tournamentService.startTournament();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
