package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService extends EventService {

    @Autowired
    private TournamentRepository tournamentRepository;

    public void pausePlayer() {
        try {
            int id = RESTSession.getUserId();
            playerRepository.pausePlayer(id);
        } catch (NotAbleToUpdateDBException e) {
            throw e;
        }
    }

    public Tournament getPlayersTournament() {
        try {
            int playerId = RESTSession.getUserId();
            int tournamentId = playerRepository.getPlayer(playerId).getTournamentId();
            return tournamentRepository.getTournament(tournamentId);
        } catch (NotInDatabaseException e) {
            throw e;
        }
    }


    public Player getPlayer(int playerId) {
        try {
            return playerRepository.getPlayer(playerId);
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

    public boolean isTournamentActive(int playerId){
        Player player = playerRepository.getPlayer(playerId);
        return  tournamentRepository.isActive(player.getTournamentId());
    }
  
    public List<? extends Game> getInactiveGames(int playerId) {
        return gameWithPlayerNamesRepository.getInActiveGames(playerId);
    }
  
    public Game getActiveGame(int playerId) {
        return gameWithPlayerNamesRepository.getActiveGame(playerId);
    }
}
