package no.ntnu.sjakkarena.mappers;

import no.ntnu.sjakkarena.data.GameWithPlayerNames;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps results from a database into a GameWithPlayerNames object
 */
public class GameWithPlayerNamesRowMapper implements RowMapper<GameWithPlayerNames> {

    /**
     * Maps results from a database into a GameWithPlayerNames object
     *
     * @param rs
     * @param i
     * @return A game with player names
     * @throws SQLException
     *
     */
    @Override
    public GameWithPlayerNames mapRow(ResultSet rs, int i) throws SQLException {
        return new GameWithPlayerNames(rs.getInt("game_id"), rs.getInt("table"),
                rs.getString("start"), rs.getString("end"),
                rs.getInt("white_player"), rs.getInt("black_player"),
                rs.getDouble("white_player_points"), rs.getBoolean("active"),
                rs.getString("white_player_name"), rs.getString("black_player_name"),
                rs.getBoolean("valid_result"));
    }
}
