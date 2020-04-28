package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.mappers.GameRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
     * Add a game result to the database
     *
     * @param gameId The game id
     * @param whitePlayerPoints The game result
     */
    public void addResult(int gameId, double whitePlayerPoints) {
        String end = LocalDateTime.now().toString();
        int affectedRows = jdbcTemplate.update("UPDATE `sjakkarena`.`game` SET `white_player_points` = " + whitePlayerPoints  + ", " +
                " `end` = \"" + end + "\" WHERE game_id = " + gameId);
        if (affectedRows != 1){
            throw new TroubleUpdatingDBException("Some problems occurred while trying to make result valid");
        }
    }

    private int addGame(Game game) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            //Adapted code from https://stackoverflow.com/questions/12882874/how-can-i-get-the-autoincremented-id-when-i-insert-a-record-in-a-table-via-jdbct
            jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                            PreparedStatement ps =
                                    connection.prepareStatement("INSERT INTO `sjakkarena`.`game` (`table`, " +
                                            "`start`, `white_player`, `black_player`, `active`) " +
                                            "VALUES ( " +  game.getTable() + ", \"" + game.getStart() + "\", " +
                                            game.getWhitePlayerId() + ", " + game.getBlackPlayerId() + ", " +
                                            game.isActive() + ") ", new String[]{"id"});
                            return ps;
                        }
                    },
                    keyHolder);
            return keyHolder.getKey().intValue();
        } catch (DataAccessException e) {
            throw new TroubleUpdatingDBException("Could not add game to database");
        }
    }

    //Adapted code from https://www.baeldung.com/spring-jdbc-jdbctemplate
    public List<Integer> addGames(List<Game> newGames) {
        List<Integer> gameIds = new ArrayList<>();
        for (Game game : newGames){
            int index = addGame(game);
            gameIds.add(index);
        }
        return gameIds;
    }


    /**
     * Sets a games valid state to invalid
     * @param gameID of game to make invalidvalid.
     */
    public void makeResultInvalid(int gameID){
        int affectedRows = jdbcTemplate.update("UPDATE sjakkarena.game SET valid_result = 0 "+
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
        int affectedRows = jdbcTemplate.update("UPDATE sjakkarena.game SET valid_result = 1 "+
                "WHERE game_id = " +gameID);
        if (affectedRows != 1){
            throw new TroubleUpdatingDBException("Some problems occurred while trying to make result valid");
        }
    }

    public void deactivateGame(int gameId){
        int affectedRows = jdbcTemplate.update("UPDATE sjakkarena.game SET `active` = 0 "+
            "WHERE game_id = " + gameId);
        if (affectedRows != 1){
            throw new TroubleUpdatingDBException("Some problems occurred while trying to deactivate game");
        }
    }

    public Game getGame(int gameId) {
        return jdbcTemplate.queryForObject("SELECT * FROM sjakkarena.game WHERE game_id = " +gameId, gameRowMapper);
    }
}
