package no.ntnu.sjakkarena.mappers;

import no.ntnu.sjakkarena.data.Player;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerRowMapper implements RowMapper<Player> {

    @Override
    public Player mapRow(ResultSet rs, int i) throws SQLException {
        Player player = new Player(rs.getInt("player_id"), rs.getString("name"),
                rs.getString("icon"));
        return player;
    }
}
