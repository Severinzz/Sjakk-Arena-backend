package no.ntnu.sjakkarena.mappers;

import no.ntnu.sjakkarena.data.Game;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps results from a database into a Game Object
 */
public class GameRowMapper implements RowMapper<Game> {

    /**
     * Maps results from a database into a Game object
     *
     * @param rs
     * @param i
     * @return A game
     * @throws SQLException
     */
    @Override
    public Game mapRow(ResultSet rs, int i) throws SQLException {
        return Game.asInDatabase(rs.getInt("game_id"), rs.getInt("table"),
                rs.getString("start"), rs.getString("end"),
                rs.getInt("white_player"), rs.getInt("black_player"),
                rs.getInt("white_player_points"), rs.getBoolean("active"),
                rs.getBoolean("valid_result"));
    }
}
