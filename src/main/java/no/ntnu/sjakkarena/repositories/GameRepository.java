package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.mappers.GameRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

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

    public void addResult(int gameId, String result) {
        LocalTime localTime = LocalTime.now();
        String end = "" + localTime.getHour() + ":" + localTime.getMinute();
        jdbcTemplate.update("UPDATE `sjakkarena`.`game` SET `result` = \"" + result + "\"," +
                " `active` = 0, `end` = \"" + end + "\" WHERE game_id = " + gameId);
    }
}
