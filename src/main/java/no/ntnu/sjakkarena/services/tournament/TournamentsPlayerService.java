package no.ntnu.sjakkarena.services.tournament;

import no.ntnu.sjakkarena.GameCreator;
import no.ntnu.sjakkarena.adaptedmonrad.AfterTournamentStartAdaptedMonrad;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.eventcreators.PlayerEventCreator;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TournamentsPlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerEventCreator playerEventCreator;

    @Autowired
    private GameCreator gameCreator;


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
            onPlayerLeftTournament(playerId, msg);
        }
        else{
            throw new TroubleUpdatingDBException("Player is not in that tournament");
        }
    }

    private void onPlayerLeftTournament(int playerId, String msg){
        gameCreator.createAndPublishNewGames(RESTSession.getUserId(), new AfterTournamentStartAdaptedMonrad());
        playerEventCreator.createAndPublishPlayerListChangeEvent(RESTSession.getUserId());
        playerEventCreator.createAndSendPlayerRemovedEvent(playerId, msg);
    }

    private boolean isPlayerInTournament(int playerId){
        int tournamentId = RESTSession.getUserId();
        return playerRepository.getPlayer(playerId).getTournamentId() == tournamentId;
    }

    public Player getPlayer(int playerId, int tournamentId) {
        if(!playerBelongInTournament(playerId, tournamentId)){
            throw new NoSuchElementException();
        }
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
    private boolean playerBelongInTournament(int playerId, int tournamentId){
        return playerRepository.playerBelongInTournament(playerId, tournamentId);
    }
}
