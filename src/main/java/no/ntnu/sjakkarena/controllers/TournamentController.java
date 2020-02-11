package no.ntnu.sjakkarena.controllers;

import com.google.gson.Gson;

import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.ImproperlyFormedDataException;
import no.ntnu.sjakkarena.exceptions.NotAbleToInsertIntoDBException;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.utils.JWTHelper;
import no.ntnu.sjakkarena.utils.Validator;
import no.ntnu.sjakkarena.utils.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TournamentController {

    @Autowired
    private TournamentRepository tournamentRepository;

    /**
     * Register a tournament
     * @param tournamentJSON tournament description in json format
     * @return HTTP status 200 ok if successfully added, HTTP status 422 UNPROCESSABLE_ENTITY otherwise
     */
    @RequestMapping(value="/new-tournament",
            method = RequestMethod.PUT)
    public Object registerTournament(@RequestBody String tournamentJSON) {
        Gson gson = new Gson();
        Tournament tournament = gson.fromJson(tournamentJSON, Tournament.class);
        try {
            Validator.validateTournament(tournament);
            int tournamentID = IDGenerator.generateTournamentID();
            tournament.setTournamentId(tournamentID);
            tournament.setAdminUUID(IDGenerator.generateAdminUUID());
            tournamentRepository.addNewTournament(tournament);
            String jwt = JWTHelper.createJWT("tournament", ""+tournamentID);
            return "{\"jwt\": \"" + jwt + "\", \"tournamentId\":" +
                    tournamentID+ "}";
        } catch (NotAbleToInsertIntoDBException | ImproperlyFormedDataException e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
