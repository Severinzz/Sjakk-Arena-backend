package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.exceptions.NotAbleToInsertIntoDBException;
import no.ntnu.sjakkarena.utils.DBInteractionHelper;
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
    public void addNewTournament(Tournament tournament, String UUID){
        String queryString = DBInteractionHelper.inputToDatabaseUpdateString(UUID, tournament.getTournamentName(), tournament.getAdminEmail(),
                tournament.getStart(), tournament.getEnd(), tournament.getTables(), tournament. getMaxRounds(),
                tournament.isActive());
    try{
            jdbcTemplate.update("INSERT INTO sjakkarena.tournament VALUES (" + queryString + ")");
        }
        catch(DataAccessException e){
            throw new NotAbleToInsertIntoDBException("Couldn't insert tournament into db");
        }
    }
}

