package no.ntnu.sjakkarena.mappers;

import no.ntnu.sjakkarena.data.GameWithPlayerNames;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InvalidGameWithPlayerNamesRowMapper implements RowMapper<GameWithPlayerNames> {

    @Override
    public GameWithPlayerNames mapRow(ResultSet rs, int i) throws SQLException {
        return new GameWithPlayerNames(rs.getInt("game_id"), rs.getInt("table"),
                rs.getString("start"), rs.getString("end"),
                rs.getInt("white_player_id"), rs.getInt("black_player_id"),
                rs.getObject("white_player_points", Integer.class), rs.getBoolean("active"),
                rs.getString("white_player_name"), rs.getString("black_player_name"));
    }
}
