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

/**
 * This class handles the business logic regarding tournaments' players
 */
@Service
public class TournamentsPlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerEventCreator playerEventCreator;

    @Autowired
    private GameCreator gameCreator;


    /**
     * Deletes the specified player
     *
     * @param playerId The id of the player to delete
     * @param msg      A message sent to the deleted player
     */
    public void deletePlayer(int playerId, String msg) {
        try {
            playerRepository.deletePlayer(playerId);
            onPlayerDeleted(playerId, msg);
        } catch (TroubleUpdatingDBException e) {
            throw new TroubleUpdatingDBException(e);
        }
    }

    /**
     * Executes necessary tasks when a player has been deleted:
     * - Creates and publishes events
     *
     * @param playerId The id of the deleted player
     * @param msg      A message sent to the deleted player
     */
    private void onPlayerDeleted(int playerId, String msg) {
        playerEventCreator.createAndPublishPlayerListChangeEvent(RESTSession.getUserId());
        playerEventCreator.createAndSendPlayerRemovedEvent(playerId, msg, true);
    }

    /**
     * Returns a player with the specified id enrolled in the specified tournament
     *
     * @param playerId     The id of the player
     * @param tournamentId The id of the tournament
     * @return A player with the specified id enrolled in the specified tournament
     */
    public Player getPlayer(int playerId, int tournamentId) {
        if (!playerBelongsToTournament(playerId, tournamentId)) {
            throw new NoSuchElementException();
        }
        try {
            return playerRepository.getPlayer(playerId);
        } catch (NotInDatabaseException e) {
            throw new NotInDatabaseException(e);
        }
    }

    /**
     * Removes a player from a tournament
     *
     * @param playerId The id of the player to remove
     * @param msg      A message sent to the removed player
     */
    public void removePlayerFromTournament(int playerId, String msg) {
        if (playerBelongsToTournament(playerId, RESTSession.getUserId())) {
            playerRepository.leaveTournament(playerId);
            onPlayerRemovedFromTournament(playerId, msg);
        } else {
            throw new TroubleUpdatingDBException("Player is not in the tournament");
        }
    }

    /**
     * Returns true if the specified player is enrolled in the specified tournament
     *
     * @param playerId     The id of the player
     * @param tournamentId The id of the tournament
     * @return True if the specified player is enrolled in the requesting tournament
     */
    private boolean playerBelongsToTournament(int playerId, int tournamentId) {
        return playerRepository.getPlayer(playerId).getTournamentId() == tournamentId;
    }

    /**
     * Executes necessary tasks when a player has left a tournament:
     * - Creates and publishes events
     * - Creates and publishes new games
     *
     * @param playerId The id of the removed player
     * @param msg      A message sent to to the removed player
     */
    private void onPlayerRemovedFromTournament(int playerId, String msg) {
        gameCreator.createAndPublishNewGames(RESTSession.getUserId(), new AfterTournamentStartAdaptedMonrad());
        playerEventCreator.createAndPublishPlayerListChangeEvent(RESTSession.getUserId());
        playerEventCreator.createAndSendPlayerRemovedEvent(playerId, msg, true);
    }

    /**
     * Returns the players enrolled in the specified tournament
     *
     * @param tournamentId The id of the tournament
     * @return The players enrolled in the specified tournament
     */
    public List<Player> getPlayersInTournament(int tournamentId) {
        return playerRepository.getPlayersInTournament(tournamentId);
    }

    /**
     * Returns the leader board of the specified tournament
     *
     * @param tournamentId The id of the tournament
     * @return The leader board of the specified tournament
     */
    public List<Player> getLeaderBoard(int tournamentId) {
        return playerRepository.getLeaderBoard(tournamentId);
    }
}
