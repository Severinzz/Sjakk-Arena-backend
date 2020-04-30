package no.ntnu.sjakkarena.services.tournament;

import no.ntnu.sjakkarena.GameCreator;
import no.ntnu.sjakkarena.adaptedmonrad.AfterTournamentStartAdaptedMonrad;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.eventcreators.GameEventCreator;
import no.ntnu.sjakkarena.eventcreators.PlayerEventCreator;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.repositories.GameRepository;
import no.ntnu.sjakkarena.repositories.GameWithPlayerNamesRepository;
import no.ntnu.sjakkarena.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * This class handles the business logic regarding tournaments' games
 */
@Service
public class TournamentsGameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameWithPlayerNamesRepository gameWithPlayerNamesRepository;

    @Autowired
    private GameCreator gameCreator;

    @Autowired
    private GameEventCreator gameEventCreator;

    @Autowired
    private PlayerEventCreator playerEventCreator;

    /**
     * Changes the specified game's result
     *
     * @param tournamentId      The id of the tournament requesting the change of result
     * @param gameId            The id of the game the result is associated with
     * @param whitePlayerPoints The number of points the white player receives
     */
    public void changeGameResult(int tournamentId, int gameId, double whitePlayerPoints) {
        if (!Validator.pointsIsValid(whitePlayerPoints)) {
            throw new IllegalArgumentException("Score: " + whitePlayerPoints + " is not valid");
        }
        try {
            gameRepository.addResult(gameId, whitePlayerPoints);
            gameRepository.makeResultValid(gameId);
            gameRepository.deactivateGame(gameId);
            onValidResultAdd(tournamentId, gameId);
        } catch (TroubleUpdatingDBException e) {
            throw new TroubleUpdatingDBException(e);
        }
    }

    /**
     * Executes necessary tasks when a result has been change
     * - Creates and publishes events
     * - Creates and publishes new games
     *
     * @param tournamentId The id of the tournament changing the games result
     * @param gameId       The id of the game the result is associated with
     */
    private void onValidResultAdd(int tournamentId, int gameId) {
        gameEventCreator.createAndPublishValidResultAddedEvent(gameId);
        playerEventCreator.createAndPublishPlayerListChangeEvent(tournamentId);
        gameCreator.createAndPublishNewGames(tournamentId, new AfterTournamentStartAdaptedMonrad());
    }

    /**
     * Returns the games associated with the specified tournament
     *
     * @param tournamentId The id of the tournament
     * @return The games associated with the specified tournament
     */
    public Collection<? extends Game> getGames(int tournamentId) {
        return gameWithPlayerNamesRepository.getGamesWithPlayerNames(tournamentId);
    }

    /**
     * Returns the active games associated with the specified tournament
     *
     * @param tournamentId The id of the tournament
     * @return The active games associated with the specified tournament
     */
    public Collection<? extends Game> getActiveGames(int tournamentId) {
        return gameWithPlayerNamesRepository.getActiveGames(tournamentId);
    }

    /**
     * Returns the games with invalid result associated with the specified tournament
     *
     * @param tournamentId The id of the tournament
     * @return The games with invalid result associated with the specified tournament
     */
    public List<? extends Game> getGamesWithInvalidResult(int tournamentId) {
        return gameWithPlayerNamesRepository.getGamesWithInvalidResult(tournamentId);
    }
}
