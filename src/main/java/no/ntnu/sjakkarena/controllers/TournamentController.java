package no.ntnu.sjakkarena.controllers;

import com.google.gson.Gson;

import no.ntnu.sjakkarena.Session;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.ImproperlyFormedDataException;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.utils.JWSHelper;
import no.ntnu.sjakkarena.utils.Validator;
import no.ntnu.sjakkarena.utils.IDGenerator;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TournamentController {

    @Autowired
    private TournamentRepository tournamentRepository;

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
    public ResponseEntity<String> getTournamentInformation(@PathVariable(name = "uuid") String UUID) {
        Tournament tournament = tournamentRepository.findTournamentByAdminUUID(UUID);
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

    /**
     * Get tournament information by providing token
     *
     * @return JSON with tournament information
     */
    @RequestMapping(value = "/tournament/information", method = RequestMethod.GET)
    public ResponseEntity<String> getTournamentInformationByToken() {
        int tournamentId = Session.getUserId();
        Tournament tournament = tournamentRepository.findTournamentById(tournamentId);
        Gson gson = new Gson();
        return new ResponseEntity<>(gson.toJson(tournament), HttpStatus.OK);
    }
}
