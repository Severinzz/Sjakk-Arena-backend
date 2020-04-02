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

@Repository
public class GameRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Game> gameRowMapper = new GameRowMapper();

    /**
     * Gets an active game where the provided players are playing
     *
     * @param player1
     * @param player2
     * @return The active game where the provided players are playing
     */
    public Game getActiveGame(int player1, int player2) {
        try {
            return jdbcTemplate.queryForObject("SELECT * " +
                    "FROM `sjakkarena`.`game` " +
                    "WHERE (`active` = 1) " +
                    "AND ((white_player = " + player1 + " " +
                    "AND  black_player = " + player2 + ")" +
                    "OR (white_player = " + player2 + " " +
                    "AND black_player = " + player1 + "))", gameRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotInDatabaseException("Player has no active games");
        }
    }

    /**
     * Gets any games with not valid result and belongs to a tournament.
     * @param TournamentID for the tournament to check for
     * @return games with invalid results.
     */
    public List<Game> getInvalidResultGames(int TournamentID) {
        try {
            List<Game> games = jdbcTemplate.query("SELECT DISTINCTROW * " +
                    "FROM sjakkarena.game " +
                    "WHERE (valid_result = 0) " +
                    "AND (white_player IN (SELECT player_id FROM sjakkarena.player WHERE tournament = " + TournamentID +") " +
                    "AND black_player IN (SELECT player_id FROM sjakkarena.player WHERE tournament = " + TournamentID +"))",
                    gameRowMapper);
            return games;
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotInDatabaseException("No games with invalid result.");
        }
    }

    /**
     * Add a game result to the database
     *
     * @param gameId The game id
     * @param whitePlayerPoints The game result
     */
    public void addResult(int gameId, double whitePlayerPoints) {
        String end = LocalDateTime.now().toString();
        int affectedRows = jdbcTemplate.update("UPDATE `sjakkarena`.`game` SET `white_player_points` = " + whitePlayerPoints + "," +
                " `active` = 0, `end` = \"" + end + "\" WHERE valid_result = 0 AND game_id = " + gameId);
        if (affectedRows != 1){
            throw new TroubleUpdatingDBException("Some problems occurred while trying to make result valid");
        }
    }

    //Adapted code from https://www.baeldung.com/spring-jdbc-jdbctemplate
    public void addGames(List<Game> newGames) {
         jdbcTemplate.batchUpdate("INSERT INTO `sjakkarena`.`game` (`table`, `start`, `white_player`, " +
                        "`black_player`, `active`) VALUES (?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, newGames.get(i).getTable());
                        ps.setString(2, newGames.get(i).getStart());
                        ps.setInt(3, newGames.get(i).getWhitePlayerId());
                        ps.setInt(4, newGames.get(i).getBlackPlayerId());
                        ps.setBoolean(5, newGames.get(i).isActive());
                    }
                    @Override
                    public int getBatchSize() {
                        return newGames.size();
                    }
                });
    }

    /**
     * Sets a games valid state to invalid.
     * @param gameID for game to invalidate.
     */
    public void invalidateResult(int gameID) {
        String sql = "UPDATE sjakkarena.game SET valid_result = 0 WHERE game_id = " + gameID;
        jdbcTemplate.update(sql);
    }

    /**
     * Sets a games valid state to valid
     * @param gameID of game to make valid.
     */
    public void makeResultValid(int gameID){
        int affectedRows = jdbcTemplate.update("UPDATE sjakkarena.game SET valid_result = 1 WHERE game_id = " +gameID);
        if (affectedRows != 1){
            throw new TroubleUpdatingDBException("Some problems occurred while trying to make result valid");
        }
    }

}
