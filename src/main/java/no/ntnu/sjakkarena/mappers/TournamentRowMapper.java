package no.ntnu.sjakkarena.mappers;

import no.ntnu.sjakkarena.data.Tournament;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TournamentRowMapper implements RowMapper<Tournament> {

    @Override
    public Tournament mapRow(ResultSet rs, int i) throws SQLException {
        Tournament tournament = new Tournament(rs.getInt("tournament_id"),
                rs.getString("tournament_name"), rs.getString("admin_email"),
                rs.getString("start"), rs.getString("end"), rs.getInt("tables"),
                rs.getInt("max_rounds"), rs.getBoolean("active"),
                rs.getString("admin_uuid"));
        return tournament;
    }
}
