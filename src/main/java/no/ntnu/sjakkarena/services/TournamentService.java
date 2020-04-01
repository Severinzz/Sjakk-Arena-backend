package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.events.PlayerRemovedEvent;
import no.ntnu.sjakkarena.events.TimeToStartTournamentEvent;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class TournamentService extends EventService {

    public void startTournament(int tournamentId) {
        try {
            tournamentRepository.setActive(tournamentId);
            onTournamentStart(tournamentId);
        } catch (NotAbleToUpdateDBException e) {
            throw e;
        }
    }

    public Tournament getTournament() {
        try {
            int tournamentId = RESTSession.getUserId();
            return tournamentRepository.getTournament(tournamentId);
        } catch (NotInDatabaseException e) {
            throw e;
        }
    }


    public Collection<? extends Game> getGames() {
        int tournamentId = RESTSession.getUserId();
        return gameWithPlayerNamesRepository.getGamesWithPlayerNames(tournamentId);
    }

    private void onTournamentStart(int tournamentId) {
        createAndPublishTournamentStartedEvent(tournamentId);
    }
    public void deletePlayer(int playerId, String msg) {
        try {
            playerRepository.deletePlayer(playerId);
            createAndPublishPlayerListChangeEvent(RESTSession.getUserId());
            sendRemovedMessage(playerId, msg);
        } catch (NotAbleToUpdateDBException e) {
            throw e;
        }
    }

    public Player getPlayer(int playerId) {
        return playerRepository.getPlayer(playerId);
    }

    public List<Player> getPlayersInTournament(int tournamentId){
        return playerRepository.getPlayersInTournament(tournamentId);
    }

    public List<Player> getLeaderBoard(int tournamentId){
        return playerRepository.getLeaderBoard(tournamentId);
    }

    public void inactivatePlayer(int playerId, String msg) {
        if(isPlayerInTournament(playerId)) {
            playerRepository.leaveTournament(playerId);
            createAndPublishPlayerListChangeEvent(RESTSession.getUserId());
            sendRemovedMessage(playerId, msg);
        }
        else{
            throw new NotAbleToUpdateDBException("Player is not in that tournament");
        }
    }

    private boolean isPlayerInTournament(int playerId){
        int tournamentId = RESTSession.getUserId();
        return playerRepository.getPlayer(playerId).getTournamentId() == tournamentId;
    }

    private void sendRemovedMessage(int playerId, String msg){
        PlayerRemovedEvent playerRemovedEvent = new PlayerRemovedEvent(this, playerId, msg);
        applicationEventPublisher.publishEvent(playerRemovedEvent);
    }

    @EventListener
    public void onTimeToStartTournament(TimeToStartTournamentEvent timeToStartTournamentEvent){
        startTournament(timeToStartTournamentEvent.getTournamentId());
    }

    public boolean isTournamentActive(int tournamentId) {
        return tournamentRepository.isActive(tournamentId);
    }
}
