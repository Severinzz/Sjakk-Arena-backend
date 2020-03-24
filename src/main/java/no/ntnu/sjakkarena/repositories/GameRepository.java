package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.mappers.GameRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
     * @param whitePlayer The player using white pieces
     * @param blackPlayer The player using black pieces
     * @return The active game where the provided players are playing
     */
    public Game getActiveGame(int whitePlayer, int blackPlayer) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM `sjakkarena`.`game` WHERE white_player = " +
                    whitePlayer + " AND " + "black_player = " + blackPlayer + " AND `active` = 1", gameRowMapper);
        } catch (NullPointerException | EmptyResultDataAccessException e) {
            return null;
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
        jdbcTemplate.update("UPDATE `sjakkarena`.`game` SET `white_player_points` = \"" + whitePlayerPoints + "\"," +
                " `active` = 0, `end` = \"" + end + "\" WHERE game_id = " + gameId);
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
     * Setting result of a game to invalid.
     * @param gameID for game to invalidate result for.
     */
    public void invalidateResult(int gameID) {
        String sql = "UPDATE sjakkarena.game SET valid_result = 0 WHERE game_id = " + gameID;
        jdbcTemplate.update(sql);
    }

    public double getResult(int gameID) {
        String sql = "SELECT 'white_player_points' FROM 'sjakkarena'.'game' WHERE 'game_id' = ?";
        String ResultDouble = (String) jdbcTemplate.queryForObject(
                sql, new Object[] { gameID }, String.class);
        return Double.valueOf(ResultDouble);
    }


    /**
     * Check if game already have a result
     * @Return true if game have a registered result, false if not.
     */
    public boolean gameHasResult(int gameID) {
        boolean hasResult = false;
        String sql = "SELECT white_player_points FROM sjakkarena.game WHERE game_id = " + gameID;
        try {
            Integer score = jdbcTemplate.queryForObject(sql, Integer.class);
            if (score >= 0) {
                hasResult = true;
            }
        } catch (NullPointerException | EmptyResultDataAccessException e) {
            hasResult = false;
        }
        return hasResult;
    }
}
