package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.GameWithPlayerNames;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.events.PlayerRemovedEvent;
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
    public void deletePlayer(int playerId, String msg) {
        try {
            playerRepository.deletePlayer(playerId);
            createAndPublishPlayerListChangeEvent(RESTSession.getUserId());
            sendRemovedMessage(playerId, msg);
        } catch (NotAbleToUpdateDBException e) {
            throw e;
        }
    }

    public String getPlayer(int playerId) {
        Player player = playerRepository.getPlayer(playerId);
        return jsonCreator.writeValueAsString(player);
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
}
