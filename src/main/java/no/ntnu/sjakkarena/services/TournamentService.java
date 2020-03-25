package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.GameWithPlayerNames;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.events.TournamentStartedEvent;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TournamentService extends EventService {


    private JSONCreator jsonCreator = new JSONCreator();

    public void startTournament() {
        try {
            tournamentRepository.setActive(RESTSession.getUserId());
            onTournamentStart();
        } catch (NotAbleToUpdateDBException e) {
            throw e;
        }
    }

    public String getTournament() {
        try {
            int tournamentId = RESTSession.getUserId();
            Tournament tournament = tournamentRepository.getTournament(tournamentId);
            return jsonCreator.writeValueAsString(tournament);
        } catch (NotInDatabaseException e) {
            throw e;
        }
    }


    public String getGamesWithPlayerNames() {
        int tournamentId = RESTSession.getUserId();
        Collection<GameWithPlayerNames> games = gameWithPlayerNamesRepository.getGamesWithPlayerNames(tournamentId);
        return jsonCreator.writeValueAsString(games);
    }

    private void onTournamentStart() {
        int tournamentId = RESTSession.getUserId();
        createAndPublishTournamentStartedEvent(tournamentId);
    }

    public void deletePlayer(int playerId) {
        try {
            playerRepository.deletePlayer(playerId);
            createAndPublishPlayerListChangeEvent(RESTSession.getUserId());
        } catch (NotAbleToUpdateDBException e) {
            throw e;
        }
    }

    public String getPlayer(int playerId) {
        Player player = playerRepository.getPlayer(playerId);
        return jsonCreator.writeValueAsString(player);
    }

    public void inactivatePlayer(int playerId) {
        playerRepository.leaveTournament(playerId);
        createAndPublishPlayerListChangeEvent(RESTSession.getUserId());
    }
}
