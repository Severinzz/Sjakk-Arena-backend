package no.ntnu.sjakkarena.services.unauthenticateduser;

import no.ntnu.sjakkarena.GameCreator;
import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.adaptedmonrad.AfterTournamentStartAdaptedMonrad;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.eventcreators.PlayerEventCreator;
import no.ntnu.sjakkarena.exceptions.NameAlreadyExistsException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.utils.PlayerIcons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class handles the business logic regarding unauthenticated players
 */
@Service
public class UnauthenticatedPlayerService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerEventCreator playerEventCreator;

    @Autowired
    private GameCreator gameCreator;

    private JSONCreator jsonCreator = new JSONCreator();

    /**
     * Handles the request to add a new player
     *
     * @param player The player to add
     * @return A JSON response to the new player
     */
    public String handleAddPlayerRequest(Player player) {
        if (playerRepository.doesPlayerExist(player)) {
            throw new NameAlreadyExistsException("Name already take, try a new one!");
        }
        try {
            player.setIcon(PlayerIcons.getRandomIcon());
            int userId = playerRepository.addNewPlayer(player);
            onNewPlayerAdd(player.getTournamentId());
            return jsonCreator.createResponseToNewPlayer(userId);
        } catch (TroubleUpdatingDBException e) {
            throw new TroubleUpdatingDBException(e);
        }
    }

    /**
     * Executes necessary tasks when a new player has been added.
     * - Create and publishes events
     * - Creates and publishes games if the tournament is active
     *
     * @param tournamentId The id of the tournament the added player is enrolled in
     */
    public void onNewPlayerAdd(int tournamentId) {
        playerEventCreator.createAndPublishNewPlayerAddedEvent(tournamentId);
        if (tournamentRepository.getTournament(tournamentId).isActive()) {
            gameCreator.createAndPublishNewGames(tournamentId, new AfterTournamentStartAdaptedMonrad());
        }
    }
}
