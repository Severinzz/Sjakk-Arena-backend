package no.ntnu.sjakkarena.services.player;

import no.ntnu.sjakkarena.GameCreator;
import no.ntnu.sjakkarena.adaptedmonrad.AfterTournamentStartAdaptedMonrad;
import no.ntnu.sjakkarena.eventcreators.PlayerEventCreator;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class handles the business logic regarding players' information
 */
@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerEventCreator playerEventCreator;

    @Autowired
    private GameCreator gameCreator;


    /**
     * Returns a player with the specified id
     *
     * @param playerId The id of the player to return
     * @return A player with the specified id
     */
    public Player getPlayer(int playerId) {
        try {
            return playerRepository.getPlayer(playerId);
        } catch (NotInDatabaseException e) {
            throw new NotInDatabaseException(e);
        }
    }

    /**
     * Pauses the specified player
     *
     * @param playerId The id of the player to pause
     */
    public void pausePlayer(int playerId) {
        try {
            playerRepository.pausePlayer(playerId);
        } catch (TroubleUpdatingDBException e) {
            throw new TroubleUpdatingDBException(e);
        }
    }

    /**
     * Unpauses the specified player
     *
     * @param playerId The id of the player to unpause
     */
    public void unpausePlayer(int playerId) {
        try {
            playerRepository.unpausePlayer(playerId);
        } catch (TroubleUpdatingDBException e) {
            throw new TroubleUpdatingDBException(e);
        }
    }

    /**
     * Lets the specified player leave the tournament
     *
     * @param playerId The id of the leaving player
     */
    public void leaveTournament(int playerId) {
        try {
            playerRepository.leaveTournament(playerId);
            onPlayerLeftTournament(playerId);
        } catch (TroubleUpdatingDBException e) {
            throw new TroubleUpdatingDBException(e);
        }
    }

    /**
     * Executes necessary tasks after the specified player has left the tournament.
     * - Creates and publishes events
     * - Creates and publishes games
     *
     * @param playerId The id of the player who left the tournament
     */
    private void onPlayerLeftTournament(int playerId) {
        playerEventCreator.createAndSendPlayerRemovedEvent(playerId, "");
        Player player = playerRepository.getPlayer(playerId);
        gameCreator.createAndPublishNewGames(player.getTournamentId(), new AfterTournamentStartAdaptedMonrad());
    }

    /**
     * Deletes the specified player
     *
     * @param playerId The id of the player to delete
     */
    public void deletePlayer(int playerId) {
        try {
            int tournamentId = playerRepository.getPlayer(playerId).getTournamentId();
            playerRepository.deletePlayer(playerId);
            playerEventCreator.createAndPublishPlayerListChangeEvent(tournamentId);
        } catch (TroubleUpdatingDBException e) {
            throw new TroubleUpdatingDBException(e);
        } catch (NotInDatabaseException e) {
            throw new NotInDatabaseException(e);
        }
    }
}
