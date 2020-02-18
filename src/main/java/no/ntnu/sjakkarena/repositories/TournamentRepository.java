package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;

import no.ntnu.sjakkarena.utils.DBInteractionHelper;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.mappers.TournamentRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
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
     *
     * @param tournament the tournament to be added
     */
    public void addNewTournament(Tournament tournament) {
        String values = "";
        String attributes = "";
        if(tournament.getEnd() == null){
            values = DBInteractionHelper.toValuesString(tournament.getId(),
                    tournament.getTournamentName(), tournament.getAdminEmail(), tournament.getStart(),
                    tournament.getTables(), tournament.getMaxRounds(), tournament.getAdminUUID(),
                    tournament.isEarlyStart());
            attributes = DBInteractionHelper.toAttributeString("tournament_id", "tournament_name", "admin_email", "start",
                    "tables", "max_rounds", "admin_uuid", "early_start");
        }
        else{
            values = DBInteractionHelper.toValuesString(tournament.getId(),
                    tournament.getTournamentName(), tournament.getAdminEmail(), tournament.getStart(), tournament.getEnd(),
                    tournament.getTables(), tournament.getMaxRounds(), tournament.getAdminUUID(),
                    tournament.isEarlyStart());
            attributes = DBInteractionHelper.toAttributeString("tournament_id", "tournament_name", "admin_email", "start", "end",
                    "tables", "max_rounds", "admin_uuid", "early_start");
        }
        executeUpdateQuery(attributes, values);
    }

    /**
     * Execute the a update query with the given attributes and values
     * @param attributes
     * @param values
     */
    private void executeUpdateQuery(String attributes, String values) {
        try {
            jdbcTemplate.update("INSERT INTO sjakkarena.tournament " +
                    attributes + " VALUES " +  values);

        } catch (DataAccessException e) {
            throw new NotAbleToUpdateDBException("Couldn't insert tournament into db");
        }
    }

    /**
     * Finds the tournament with the given id.
     * @param id the id of the tournament to be found
     * @return The tournament with the given id. If no such tournament was found in the database, an "empty" tournament
     * object is returned.
     */
    public Tournament findTournamentById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM sjakkarena.tournament WHERE " +
                    "tournament_id = " + id, Tournament.class);
        }
        catch(NullPointerException e){
            return new Tournament();
        }
    }

    /**
     * Finds the tournament with the given admin uuid.
     * @param adminUUID the adminUUID of the tournament to be found
     * @return The tournament with the given UUID. If no such tournament was found in the database, an "empty" tournament
     * object is returned.
     */
    public Tournament findTournamentByAdminUUID(String adminUUID) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM sjakkarena.tournament WHERE " +
                    "admin_uuid = \"" + adminUUID + "\"", rowMapper);
        }
        catch(NullPointerException| EmptyResultDataAccessException e){
            return new Tournament();
        }
    }
}

