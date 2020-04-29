package no.ntnu.sjakkarena.services.player;

import no.ntnu.sjakkarena.GameCreator;
import no.ntnu.sjakkarena.adaptedmonrad.AfterTournamentStartAdaptedMonrad;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.ResultUpdateRequest;
import no.ntnu.sjakkarena.eventcreators.GameEventCreator;
import no.ntnu.sjakkarena.eventcreators.PlayerEventCreator;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.repositories.GameRepository;
import no.ntnu.sjakkarena.repositories.GameWithPlayerNamesRepository;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.utils.RESTSession;
import no.ntnu.sjakkarena.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class handles the business logic regarding players' games
 */
@Service
public class PlayersGameService {

    @Autowired
    private GameWithPlayerNamesRepository gameWithPlayerNamesRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameEventCreator gameEventCreator;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerEventCreator playerEventCreator;

    @Autowired
    private GameCreator gameCreator;


    /**
     * Returns the specified player's inactive games
     *
     * @param playerId The id of the player
     * @return The specified player's inactive games
     */
    public List<? extends Game> getInactiveGames(int playerId) {
        return gameWithPlayerNamesRepository.getInActiveGames(playerId);
    }

    /**
     * Returns the specified player's active games
     *
     * @param playerId The id of the player
     * @return The specified player's active games
     */
    public Game getActiveGame(int playerId) {
        try {
            return gameWithPlayerNamesRepository.getActiveGame(playerId);
        } catch (EmptyResultDataAccessException e) {
            throw e;
        }
    }

    /**
     * Adds a result
     *
     * @param resultUpdateRequest A request to update a result
     */
    public void addResult(ResultUpdateRequest resultUpdateRequest) {
        if (!Validator.pointsIsValid(resultUpdateRequest.getResult())) {
            throw new IllegalArgumentException("Not a valid result");
        }
        try {
            Game game = gameRepository.getActiveGame(RESTSession.getUserId(), resultUpdateRequest.getOpponent());
            gameRepository.addResult(game.getGameId(), resultUpdateRequest.getResult());
            onSuggestedResult(resultUpdateRequest.getOpponent(), resultUpdateRequest.getResult(), game.getGameId());
        } catch (NotInDatabaseException e) {
            throw e;
        }
    }

    /**
     * Executes necessary tasks when a result has been suggested:
     * - Creates and publishes events
     *
     * @param opponentId The id of the opponent of the player suggesting the result
     * @param result     The suggested result
     * @param gameId     The id of the game associated with the result.
     */
    private void onSuggestedResult(int opponentId, double result, int gameId) {
        gameEventCreator.createAndPublishResultSuggestedEvent(opponentId, result, gameId);
    }


    /**
     * Validates the specified game's result
     *
     * @param gameID The id of the game
     */
    public void setGameResultValid(int gameID) {
        Game game = gameRepository.getGame(gameID);
        if (game.getWhitePlayerId() != RESTSession.getUserId() && game.getBlackPlayerId() != RESTSession.getUserId()) {
            throw new IllegalArgumentException("Requesting user is not playing the game with gameId " + gameID);
        }
        gameRepository.makeResultValid(gameID);
        gameRepository.deactivateGame(gameID);
        onResultValidated(gameID);
    }

    /**
     * Executes necessary tasks when a result has been validated:
     * - Creates and publishes events
     *
     * @param gameId The id of the game associated with the validated result
     */
    private void onResultValidated(int gameId) {
        Game game = gameRepository.getGame(gameId);
        int tournamentId = playerRepository.getPlayer(game.getWhitePlayerId()).getTournamentId();
        playerEventCreator.createAndPublishPlayerListChangeEvent(tournamentId);
        gameEventCreator.createAndPublishValidResultAddedEvent(gameId);
        gameCreator.createAndPublishNewGames(tournamentId, new AfterTournamentStartAdaptedMonrad());
    }


    /**
     * Invalidates the specified game's result
     *
     * @param gameId The id of the game
     */
    public void invalidateResult(int gameId) {
        gameRepository.makeResultInvalid(gameId);
        onResultInvalidated(gameId);
    }

    /**
     * Executes necessary tasks when a result has been invalidated:
     * - Creates and publishes events
     *
     * @param gameId The id of the game associated with the invalidated result
     */
    private void onResultInvalidated(int gameId) {
        gameEventCreator.createAnPublishInvalidResultEvent(gameId);
    }

    /**
     * Ends a game due to a removal of a player
     *
     * @param playerId The id of the removed player
     */
    public void endGameDueToPlayerRemoval(int playerId) {
        try {
            Game game = gameRepository.getActiveGame(playerId);
            int result = game.getBlackPlayerId() == playerId ? 1 : 0;
            gameRepository.addResult(game.getGameId(), result);
            gameRepository.deactivateGame(game.getGameId());
            onGameEndedDueToPlayerRemoval(playerId, game);
        } catch (NotInDatabaseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes necessary tasks when a game is ended due to the removal of a player:
     * - Creates and publishes events
     *
     * @param playerId The id of the removed player
     * @param game     The game the player played when he was removed.
     */
    private void onGameEndedDueToPlayerRemoval(int playerId, Game game) {
        Player player = playerRepository.getPlayer(playerId);
        playerEventCreator.createAndPublishPlayerListChangeEvent(player.getTournamentId());
        gameEventCreator.createAndPublishValidResultAddedEvent(game.getGameId());
    }

    /**
     * Returns the specified player's opponent's id
     *
     * @param playerId The id of the player
     * @return The opponent of the specified player
     */
    public int getOpponentId(int playerId) {
        try {
            Game game = gameRepository.getActiveGame(playerId);
            return game.getWhitePlayerId() == playerId ? game.getBlackPlayerId() : game.getWhitePlayerId();
        } catch (NotInDatabaseException e) {
            throw new NotInDatabaseException(e);
        }
    }

    /**
     * Returns true if the specified player has an active game
     *
     * @param playerId The id of the player
     * @return True if the specified player has an active game
     */
    public boolean hasActiveGame(int playerId) {
        try {
            Game game = gameRepository.getActiveGame(playerId);
            return true;
        } catch (NotInDatabaseException e) {
            return false;
        }
    }
}
