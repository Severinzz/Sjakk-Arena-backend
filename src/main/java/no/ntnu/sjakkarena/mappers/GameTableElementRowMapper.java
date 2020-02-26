package no.ntnu.sjakkarena.mappers;

import no.ntnu.sjakkarena.data.GameTableElement;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameTableElementRowMapper implements RowMapper<GameTableElement> {

    @Override
    public GameTableElement mapRow(ResultSet rs, int i) throws SQLException {
        return new GameTableElement(rs.getInt("game_id"), rs.getInt("table"),
                rs.getString("result"), rs.getBoolean("active"),
                rs.getString("start"), rs.getString("end"), rs.getInt("white_player_id"),
                rs.getString("white_player_name"), rs.getInt("black_player_id"),
                rs.getString("black_player_name"));
    }
}
