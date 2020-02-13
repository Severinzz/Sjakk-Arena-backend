package no.ntnu.sjakkarena.controllers;

import com.google.gson.Gson;
import no.ntnu.sjakkarena.Session;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.exceptions.NotAbleToInsertIntoDBException;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.utils.JWSHelper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Register a new user
     *
     * @param userJSON A description of the user to be added in json format
     * @return
     */
    @RequestMapping(value = "/new-player", method = RequestMethod.PUT)
    public ResponseEntity<String> registerUser(@RequestBody String userJSON) {
        Gson gson = new Gson();
        Player player = gson.fromJson(userJSON, Player.class);
        try {
            int userId = playerRepository.addNewPlayer(player);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("jwt", JWSHelper.createJWS("PLAYER", "" + userId));
            return new ResponseEntity<>(jsonResponse.toString(), HttpStatus.OK);
        } catch (NotAbleToInsertIntoDBException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/tournament/player-names", method=RequestMethod.GET)
    public ResponseEntity<String> getPlayerNames(){
        int tournamentId = Session.getUserId();
        Collection<String> playerNames = playerRepository.getAllPlayerNamesInTournament(tournamentId);
        return new ResponseEntity<>(new Gson().toJson(playerNames), HttpStatus.OK);
    }
}
