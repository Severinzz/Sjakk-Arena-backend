package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.GameTableElement;
import no.ntnu.sjakkarena.mappers.GameRowMapper;
import no.ntnu.sjakkarena.mappers.GameTableElementRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public class GameRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Game> gameRowMapper = new GameRowMapper();

    private RowMapper<GameTableElement> gameTableElementRowMapper = new GameTableElementRowMapper();

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

    /**
     * Get games by tournament
     *
     * @param tournamentId The id of the tournament to which the games belong.
     * @return The games in the tournament
     */
    public Collection<GameTableElement> getGames(int tournamentId) {
        return jdbcTemplate.query("SELECT `game_id`, `table`, `result`, `game`.`active`, `game`.`start`, " +
                "`game`.`end`, `white_player` AS `white_player_id`, `white`.`name` AS `white_player_name`, " +
                "`black_player` AS `black_player_id`, `black`.`name` AS `black_player_name`" +
                " FROM `sjakkarena`.`game` AS `game`, `sjakkarena`.`player` AS white, `sjakkarena`.`player` AS `black`, " +
                " `sjakkarena`.`tournament` AS `tournament` " +
                " WHERE `game`.`white_player` = `white`.`player_id` AND " +
                "`game`.`black_player` = `black`.`player_id` AND `tournament`.`tournament_id` = `white`.`tournament`" +
                " AND `tournament`.`tournament_id` = " + tournamentId +
                " ORDER BY `game`.`start` DESC", gameTableElementRowMapper);
    }

    // TODO change to getActiveGames in tournament
    public Collection<GameTableElement> getGamesByPlayer(int playerId) {
        return jdbcTemplate.query("SELECT `game_id`, `table`, `result`, `game`.`active`, `game`.`start`, " +
                "`game`.`end`, `white_player` AS `white_player_id`, `white`.`name` AS `white_player_name`, " +
                "`black_player` AS `black_player_id`, `black`.`name` AS `black_player_name`" +
                " FROM `sjakkarena`.`game` AS `game`, `sjakkarena`.`player` AS white, `sjakkarena`.`player` AS `black`" +
                " WHERE `game`.`white_player` = `white`.`player_id` AND " +
                " `game`.`black_player` = `black`.`player_id`" +
                " AND (`white`.`player_id` = " + playerId + " OR `black`.`player_id` = " + playerId + ")" +
                " ORDER BY `game`.`start` DESC", gameTableElementRowMapper);
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
}
