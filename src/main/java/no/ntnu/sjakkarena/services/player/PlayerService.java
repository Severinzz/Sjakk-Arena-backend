package no.ntnu.sjakkarena.services.player;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.services.EventService;
import org.springframework.stereotype.Service;

@Service
public class PlayerService extends EventService {


    public void pausePlayer(int playerId) {
        try {
            playerRepository.pausePlayer(playerId);
        } catch (TroubleUpdatingDBException e) {
            throw new TroubleUpdatingDBException(e);
        }
    }

    public Player getPlayer(int playerId) {
        try {
            return playerRepository.getPlayer(playerId);
        } catch (NotInDatabaseException e) {
            throw new NotInDatabaseException(e);
        }
    }

    public void unpausePlayer(int playerId) {
        try {
            playerRepository.unpausePlayer(playerId);
        } catch (TroubleUpdatingDBException e) {
            throw new TroubleUpdatingDBException(e);
        }
    }

    public void leaveTournament(int playerId) {
        try {
            playerRepository.leaveTournament(playerId);
        } catch (TroubleUpdatingDBException e) {
            throw new TroubleUpdatingDBException(e);
        }
    }

    public void deletePlayer(int playerId) {
        try {
            int tournamentId = playerRepository.getPlayer(playerId).getTournamentId();
            playerRepository.deletePlayer(playerId);
            createAndPublishPlayerListChangeEvent(tournamentId);
        } catch (TroubleUpdatingDBException e) {
            throw new TroubleUpdatingDBException(e);
        } catch (NotInDatabaseException e){
            throw new NotInDatabaseException(e);
        }
    }
}
