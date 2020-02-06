package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.Exceptions.NotAbleToInsertIntoDBException;
import no.ntnu.sjakkarena.Tools;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.mappers.TournamentRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Repository containing methods for handling tournament data
 */
@Repository
public class TournamentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Tournament> rowMapper = new TournamentRowMapper();

    /**
     * Adds a new tournament to the database
     * @param tournament the tournament to be added
     */
    public void addNewTournament(Tournament tournament){
        String queryString = Tools.inputToDatabaseUpdateString(tournament.getTournamentName(), tournament.getAdminEmail(),
                tournament.getStart(), tournament.getEnd(), tournament.getTables(), tournament. getMaxRounds(),
                tournament.isActive(), tournament.getAdminUUID());
    try{
            jdbcTemplate.update("INSERT INTO sjakkarena.tournament (tournament_name, admin_email, " +
                    "start, end, tables, max_rounds, active, admin_uuid) VALUES (" + queryString + ")");
        }
        catch(DataAccessException e){
            throw new NotAbleToInsertIntoDBException("Couldn't insert tournament into db");
        }
    }
}

