package no.ntnu.sjakkarena.controllers;

import com.google.gson.Gson;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TournamentController {

    @Autowired
    private TournamentRepository tournamentRepository;

    /**
     * Register a tournament
     * @param tournamentJSON tournament description in json format
     */
    @RequestMapping(value="/new-tournament",
            method = RequestMethod.PUT)
    public void registerTournament(@RequestBody String tournamentJSON){
        Gson gson = new Gson();
        Tournament tournament = gson.fromJson(tournamentJSON, Tournament.class);
        // TODO add validation
        // TODO add try-catch
        tournamentRepository.addNewTournament(tournament);
    }
}
