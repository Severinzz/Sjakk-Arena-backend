package no.ntnu.sjakkarena.controllers;

import com.google.gson.Gson;
import no.ntnu.sjakkarena.Exceptions.ImproperlyFormedDataException;
import no.ntnu.sjakkarena.Exceptions.NotAbleToInsertIntoDBException;
import no.ntnu.sjakkarena.utils.IDGenerator;
import no.ntnu.sjakkarena.utils.Validator;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
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
            return tournamentID;
        } catch (NotAbleToInsertIntoDBException | ImproperlyFormedDataException e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
