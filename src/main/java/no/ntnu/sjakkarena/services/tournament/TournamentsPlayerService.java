package no.ntnu.sjakkarena.services.tournament;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.events.playerevents.PlayerRemovedEvent;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.services.EventService;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentsPlayerService extends EventService {


    public void deletePlayer(int playerId, String msg) {
        try {
            playerRepository.deletePlayer(playerId);
            createAndPublishPlayerListChangeEvent(RESTSession.getUserId());
            sendRemovedMessage(playerId, msg);
        } catch (TroubleUpdatingDBException e) {
            throw new TroubleUpdatingDBException(e);
        }
    }

    private void sendRemovedMessage(int playerId, String msg){
        PlayerRemovedEvent playerRemovedEvent = new PlayerRemovedEvent(this, playerId, msg);
        applicationEventPublisher.publishEvent(playerRemovedEvent);
    }

    public void inactivatePlayer(int playerId, String msg) {
        if(isPlayerInTournament(playerId)) {
            playerRepository.leaveTournament(playerId);
            createAndPublishPlayerListChangeEvent(RESTSession.getUserId());
            sendRemovedMessage(playerId, msg);
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

}
