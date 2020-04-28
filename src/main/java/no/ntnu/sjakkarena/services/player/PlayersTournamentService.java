package no.ntnu.sjakkarena.services.player;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class handles the business logic regarding the players' tournament
 */
@Service
public class PlayersTournamentService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    /**
     * Returns the tournament of the specified player
     *
     * @param playerId The id of the player
     * @return The tournament of the specified player
     */
    public Tournament getTournament(int playerId) {
        try {
            int tournamentId = playerRepository.getPlayer(playerId).getTournamentId();
            return tournamentRepository.getTournament(tournamentId);
        } catch (NotInDatabaseException e) {
            throw new NotInDatabaseException(e);
        }
    }

    /**
     * Returns true if the specified player's tournament is active
     *
     * @param playerId The id of the player
     * @return True if the specified player's tournament is active
     */
    public boolean isTournamentActive(int playerId) {
        try {
            Player player = playerRepository.getPlayer(playerId);
            return tournamentRepository.isActive(player.getTournamentId());
        } catch (NotInDatabaseException e) {
            throw new NotInDatabaseException(e);
        }
    }
}
