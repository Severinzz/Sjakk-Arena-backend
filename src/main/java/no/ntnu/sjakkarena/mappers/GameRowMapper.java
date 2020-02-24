package no.ntnu.sjakkarena.mappers;

import no.ntnu.sjakkarena.data.Game;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameRowMapper implements RowMapper<Game> {

    @Override
    public Game mapRow(ResultSet rs, int i) throws SQLException {
        return new Game(rs.getInt("game_id"), rs.getInt("table"),
                rs.getString("start"), rs.getString("end"),
                rs.getInt("white_player"), rs.getInt("black_player"),
                rs.getString("result"), rs.getBoolean("active"));
    }
}