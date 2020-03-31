package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService extends EventService {

    @Autowired
    private TournamentRepository tournamentRepository;

    private JSONCreator jsonCreator = new JSONCreator();

    public void pausePlayer() {
        try {
            int id = RESTSession.getUserId();
            playerRepository.pausePlayer(id);
        } catch (NotAbleToUpdateDBException e) {
            throw e;
        }
    }

    public String getPlayersTournament() {
        try {
            int playerId = RESTSession.getUserId();
            int tournamentId = playerRepository.getPlayer(playerId).getTournamentId();
            Tournament tournament = tournamentRepository.getTournament(tournamentId);
            return jsonCreator.filterPlayersTournamentInformationAndReturnAsJson(tournament);
        } catch (NotInDatabaseException e) {
            throw e;
        }
    }


    public String getPlayer() {
        try {
            int playerId = RESTSession.getUserId();
            Player player = playerRepository.getPlayer(playerId);
            return jsonCreator.filterPlayerInformationAndReturnAsJson(player);
        } catch (NotInDatabaseException e) {
            throw e;
        }
    }

    public void unpausePlayer() {
        try {
            playerRepository.unpausePlayer(RESTSession.getUserId());
        } catch (NotAbleToUpdateDBException e) {
            throw e;
        }
    }

    public void setInactive() {
        try {
            playerRepository.leaveTournament(RESTSession.getUserId());
        } catch (NotAbleToUpdateDBException e) {
            throw e;
        }
    }

    public void deletePlayer() {
        try {
            int playerId = RESTSession.getUserId();
            int tournamentId = playerRepository.getPlayer(playerId).getTournamentId();
            playerRepository.deletePlayer(playerId);
            createAndPublishPlayerListChangeEvent(tournamentId);
        } catch (NotAbleToUpdateDBException e) {
            throw e;
        }
    }

    public String getIsTournamentActiveResponse(int playerId){
        Player player = playerRepository.getPlayer(playerId);
        boolean active =  tournamentRepository.isActive(player.getTournamentId());
        return jsonCreator.createResponseToTournamentStateRequester(active);
    }
  
    public List<? extends Game> getInactiveGames(int playerId) {
        return gameWithPlayerNamesRepository.getInActiveGames(playerId);
    }
  
    public Game getActiveGame(int playerId) {
        return gameWithPlayerNamesRepository.getActiveGame(playerId);
    }
}
