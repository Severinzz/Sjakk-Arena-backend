package no.ntnu.sjakkarena.controllers;

import com.google.gson.Gson;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.ImproperlyFormedDataException;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.utils.IDGenerator;
import no.ntnu.sjakkarena.utils.JWSHelper;
import no.ntnu.sjakkarena.utils.PlayerIcons;
import no.ntnu.sjakkarena.utils.Validator;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserManagementController {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Register a new user
     *
     * @param userJSON A description of the user to be added in json format
     * @return
     */
    @RequestMapping(value = "/new-player", method = RequestMethod.POST)
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
     * Register a tournament
     *
     * @param tournamentJSON tournament description in json format
     * @return HTTP status 200 ok if successfully added + jwt and tournamentID,
     * HTTP status 422 UNPROCESSABLE_ENTITY otherwise
     */
    @RequestMapping(value = "/new-tournament",
            method = RequestMethod.POST)
    public ResponseEntity<String> registerTournament(@RequestBody String tournamentJSON) {
        Gson gson = new Gson();
        Tournament tournament = gson.fromJson(tournamentJSON, Tournament.class);
        try {
            Validator.tournamentIsValid(tournament);
            int tournamentID = IDGenerator.generateTournamentID();
            tournament.setId(tournamentID);
            tournament.setAdminUUID(IDGenerator.generateAdminUUID());
            tournamentRepository.addNewTournament(tournament);
            String jws = JWSHelper.createJWS("TOURNAMENT", "" + tournamentID);
            /* TODO remove comment to send email when a tournament is registered
            EmailSender emailSender = new EmailSender();
            emailSender.sendEmail(tournament.getAdminEmail(), tournament.getTournamentName(),
                    "Her er din turneringsID: " + tournament.getAdminUUID() +
                            ". Bruk denne til å gå til din turneringsside");
             */
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jwt", jws);
            jsonObject.put("tournament_id", tournamentID);
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        } catch (NotAbleToUpdateDBException | ImproperlyFormedDataException | NullPointerException e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/tournament-information/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<String> getTournamentByUUID(@PathVariable(name = "uuid") String UUID) {
        Tournament tournament = tournamentRepository.getTournament(UUID);
        if (tournament.getAdminUUID() == null) {
            return new ResponseEntity<>("TournamentId is incorrect", HttpStatus.BAD_REQUEST);
        } else {
            //Create JWS
            String jws = JWSHelper.createJWS("TOURNAMENT", "" + tournament.getId());
            //JSON with tournament information
            Gson gson = new Gson();
            String tournamentJson = gson.toJson(tournament);
            //JSON with JWS and tournamentInformation
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jwt", jws);
            jsonObject.put("tournament", tournamentJson);
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        }
    }
}
