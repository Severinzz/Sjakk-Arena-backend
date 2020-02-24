package no.ntnu.sjakkarena.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.ntnu.sjakkarena.Session;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.utils.JWSHelper;
import no.ntnu.sjakkarena.utils.PlayerIcons;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        player.setIcon(PlayerIcons.getRandomFontAwesomeIcon());
        try {
            int userId = playerRepository.addNewPlayer(player);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("jwt", JWSHelper.createJWS("PLAYER", "" + userId));
            return new ResponseEntity<>(jsonResponse.toString(), HttpStatus.OK);
        } catch (NotAbleToUpdateDBException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Returns all the names of the players in the tournament
     *
     * @return A json with the player ids mapped to their names
     */
    @RequestMapping(value = "/tournament/player-lobby-information", method = RequestMethod.GET)
    public ResponseEntity<String> getPlayerNames() {
        int tournamentId = Session.getUserId();
        Collection<Player> players = playerRepository.getAllPlayerNamesInTournament(tournamentId);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return new ResponseEntity<>(gson.toJson(players), HttpStatus.OK);
    }

    /**
     * Deletes the player with the given ID
     *
     * @return 200 OK if successfully deleted, otherwise 400 BAD REQUEST
     */
    @RequestMapping(value = "/tournament/delete-player/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deletePlayer(@PathVariable(name = "id") int id) {
        try {
            playerRepository.deletePlayer(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotAbleToUpdateDBException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }
}
