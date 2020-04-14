package no.ntnu.sjakkarena.services.tournament;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.eventcreators.PlayerEventCreator;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentsPlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerEventCreator playerEventCreator;


    public void deletePlayer(int playerId, String msg) {
        try {
            playerRepository.deletePlayer(playerId);
            playerEventCreator.createAndPublishPlayerListChangeEvent(RESTSession.getUserId());
            playerEventCreator.createAndSendPlayerRemovedEvent(playerId, msg);
        } catch (TroubleUpdatingDBException e) {
            throw new TroubleUpdatingDBException(e);
        }
    }

    public void inactivatePlayer(int playerId, String msg) {
        if(isPlayerInTournament(playerId)) {
            playerRepository.leaveTournament(playerId);
            playerEventCreator.createAndPublishPlayerListChangeEvent(RESTSession.getUserId());
            playerEventCreator.createAndSendPlayerRemovedEvent(playerId, msg);
        }
        else{
            throw new TroubleUpdatingDBException("Player is not in that tournament");
        }
    }

    private boolean isPlayerInTournament(int playerId){
        int tournamentId = RESTSession.getUserId();
        return playerRepository.getPlayer(playerId).getTournamentId() == tournamentId;
    }

    public Player getPlayer(int playerId) {
        try {
            return playerRepository.getPlayer(playerId);
        } catch (NotInDatabaseException e){
            throw new NotInDatabaseException(e);
        }
    }

    public List<Player> getPlayersInTournament(int tournamentId){
        return playerRepository.getPlayersInTournament(tournamentId);
    }

    public List<Player> getLeaderBoard(int tournamentId){
        return playerRepository.getLeaderBoard(tournamentId);
    }

    /**
     * Check if player ID belongs to tournament ID.
     * @param playerId player ID to check for
     * @return true if player ID belongs to tournament ID
     */
    public Boolean playerBelongsInTournament(int playerId) {
        int tournamentId = RESTSession.getUserId();
        if(!playerRepository.playerBelongInTournament(playerId, tournamentId)) {
            throw new NotInDatabaseException("Player, host combo does not exist");
        }
        return null;
    }
}
