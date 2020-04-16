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

    private static final String DATABASE = System.getenv("SJAKK_ARENA_DATABASE");


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
                    "FROM " + DATABASE + ".`game` " +
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
     * Add a game result to the database
     *
     * @param gameId The game id
     * @param whitePlayerPoints The game result
     */
    public void addResult(int gameId, double whitePlayerPoints) {
        String end = LocalDateTime.now().toString();
        int affectedRows = jdbcTemplate.update("UPDATE " + DATABASE + ".`game` SET `white_player_points` = " + whitePlayerPoints  + ", " +
                " `end` = \"" + end + "\" WHERE game_id = " + gameId);
        if (affectedRows != 1){
            throw new TroubleUpdatingDBException("Some problems occurred while trying to make result valid");
        }
    }

    //Adapted code from https://www.baeldung.com/spring-jdbc-jdbctemplate
    public void addGames(List<Game> newGames) {
         jdbcTemplate.batchUpdate("INSERT INTO " + DATABASE + ".`game` (`table`, `start`, `white_player`, " +
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
     * Sets a games valid state to invalid
     * @param gameID of game to make invalidvalid.
     */
    public void makeResultInvalid(int gameID){
        int affectedRows = jdbcTemplate.update("UPDATE " + DATABASE + ".game SET valid_result = 0 "+
                "WHERE game_id = " +gameID);
        if (affectedRows != 1){
            throw new TroubleUpdatingDBException("Some problems occurred while trying to make result invalid");
        }
    }

    /**
     * Sets a games valid state to valid
     * @param gameID of game to make valid.
     */
    public void makeResultValid(int gameID){
        int affectedRows = jdbcTemplate.update("UPDATE " + DATABASE + ".game SET valid_result = 1 "+
                "WHERE game_id = " +gameID);
        if (affectedRows != 1){
            throw new TroubleUpdatingDBException("Some problems occurred while trying to make result valid");
        }
    }

    public void deactivateGame(int gameId){
        int affectedRows = jdbcTemplate.update("UPDATE " + DATABASE + ".game SET `active` = 0 "+
            "WHERE game_id = " + gameId);
        if (affectedRows != 1){
            throw new TroubleUpdatingDBException("Some problems occurred while trying to deactivate game");
        }
    }

    public Game getGame(int gameId) {
        return jdbcTemplate.queryForObject("SELECT * FROM " + DATABASE + ".game WHERE game_id = " +gameId, gameRowMapper);
    }
}
