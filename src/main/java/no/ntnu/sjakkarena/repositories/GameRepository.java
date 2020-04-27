package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.mappers.GameRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository containing methods for handling game data
 */
@Repository
public class GameRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Game> gameRowMapper = new GameRowMapper();

    /**
     * Gets an active game where the two players are playing
     *
     * @param player1Id The id of one of the playing players
     * @param player2Id The id of the other playing player
     * @return The active game where the players are playing
     */
    public Game getActiveGame(int player1Id, int player2Id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * " +
                    "FROM `sjakkarena`.`game` " +
                    "WHERE (`active` = 1) " +
                    "AND ((white_player = " + player1Id + " " +
                    "AND  black_player = " + player2Id + ")" +
                    "OR (white_player = " + player2Id + " " +
                    "AND black_player = " + player1Id + "))", gameRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotInDatabaseException("Player has no active games");
        }
    }

    /**
     * Gets a player's active game
     *
     * @param playerId The id of the player
     * @return A player's active game.
     */
    public Game getActiveGame(int playerId) {
        try {
            return jdbcTemplate.queryForObject("SELECT * " +
                    "FROM `sjakkarena`.`game` " +
                    "WHERE (`active` = 1) " +
                    "AND (white_player = " + playerId +
                    "\tOR black_player = " + playerId + ")", gameRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotInDatabaseException("Player has no active games");
        }
    }

    /**
     * Adds a game result to the database
     *
     * @param gameId            The id of the game the result is associated with
     * @param whitePlayerPoints The number of points the white player received
     */
    public void addResult(int gameId, double whitePlayerPoints) {
        String end = LocalDateTime.now().toString();
        int affectedRows = jdbcTemplate.update("UPDATE `sjakkarena`.`game` SET `white_player_points` = " + whitePlayerPoints + ", " +
                " `end` = \"" + end + "\" WHERE game_id = " + gameId);
        if (affectedRows != 1) {
            throw new TroubleUpdatingDBException("Some problems occurred while trying to make result valid");
        }
    }

    /**
     * Adds multiple games to the database
     *
     * @param games The games to be added
     */
    //Adapted code from https://www.baeldung.com/spring-jdbc-jdbctemplate
    public void addGames(List<Game> games) {
        jdbcTemplate.batchUpdate("INSERT INTO `sjakkarena`.`game` (`table`, `start`, `white_player`, " +
                        "`black_player`, `active`) VALUES (?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, games.get(i).getTable());
                        ps.setString(2, games.get(i).getStart());
                        ps.setInt(3, games.get(i).getWhitePlayerId());
                        ps.setInt(4, games.get(i).getBlackPlayerId());
                        ps.setBoolean(5, games.get(i).isActive());
                    }

                    @Override
                    public int getBatchSize() {
                        return games.size();
                    }
                });
    }


    /**
     * Sets a game's result to invalid
     *
     * @param gameID The id of the game which will have an invalid result.
     */
    public void makeResultInvalid(int gameID) {
        int affectedRows = jdbcTemplate.update("UPDATE sjakkarena.game SET valid_result = 0 " +
                "WHERE game_id = " + gameID);
        if (affectedRows != 1) {
            throw new TroubleUpdatingDBException("Some problems occurred while trying to make result invalid");
        }
    }

    /**
     * Sets a game's result to valid
     *
     * @param gameID The id of the game which will have a valid result.
     */
    public void makeResultValid(int gameID) {
        int affectedRows = jdbcTemplate.update("UPDATE sjakkarena.game SET valid_result = 1 " +
                "WHERE game_id = " + gameID);
        if (affectedRows != 1) {
            throw new TroubleUpdatingDBException("Some problems occurred while trying to make result valid");
        }
    }

    /**
     * Deactivates a game
     *
     * @param gameId The id of the game to be deactivated
     */
    public void deactivateGame(int gameId) {
        int affectedRows = jdbcTemplate.update("UPDATE sjakkarena.game SET `active` = 0 " +
                "WHERE game_id = " + gameId);
        if (affectedRows != 1) {
            throw new TroubleUpdatingDBException("Some problems occurred while trying to deactivate game");
        }
    }

    /**
     * Gets a game by the game's id
     *
     * @param gameId The id of the game to be returned
     * @return a game
     */
    public Game getGame(int gameId) {
        return jdbcTemplate.queryForObject("SELECT * FROM sjakkarena.game WHERE game_id = " + gameId, gameRowMapper);
    }
}
