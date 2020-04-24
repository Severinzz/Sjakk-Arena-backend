package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.mappers.TournamentRowMapper;
import no.ntnu.sjakkarena.utils.DBInteractionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository containing methods for handling tournament data
 */
@Repository
public class TournamentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Tournament> tournamentRowMapper = new TournamentRowMapper();

    private static final String DATABASE = System.getenv("SJAKK_ARENA_DATABASE");


    /**
     * Adds a new tournament to the database
     *
     * @param tournament the tournament to be added
     */
    public void addNewTournament(Tournament tournament) {
        String values = "";
        String attributes = "";
        if (tournament.getEnd() == null) {
            values = DBInteractionHelper.toValuesString(tournament.getId(),
                    tournament.getTournamentName(), tournament.getAdminEmail(), tournament.getStart(),
                    tournament.getTables(), tournament.getMaxRounds(), tournament.getAdminUUID(),
                    tournament.isEarlyStart());
            attributes = "(`tournament_id`, `tournament_name`, `admin_email`, `start`, " +
                    "`tables`, `max_rounds`, `admin_uuid`, `early_start`)";
        } else {
            values = DBInteractionHelper.toValuesString(tournament.getId(),
                    tournament.getTournamentName(), tournament.getAdminEmail(), tournament.getStart(), tournament.getEnd(),
                    tournament.getTables(), tournament.getMaxRounds(), tournament.getAdminUUID(),
                    tournament.isEarlyStart());
            attributes = "(`tournament_id`, `tournament_name`, `admin_email`, `start`, `end`, " +
                    "`tables`, `max_rounds`, `admin_uuid`, `early_start`)";
        }
        executeUpdateQuery(attributes, values);
    }

    /**
     * Execute the a update query with the given attributes and values
     *
     * @param attributes
     * @param values
     */
    private void executeUpdateQuery(String attributes, String values) {
        try {
            jdbcTemplate.update("INSERT INTO " + DATABASE + ".`tournament` " +
                    attributes + " VALUES " + values);

        } catch (DataAccessException e) {
            throw new TroubleUpdatingDBException("Couldn't insert tournament into db");
        }
    }

    /**
     * Finds the tournament with the given id.
     *
     * @param tournamentId the id of the tournament to be found
     * @return The tournament with the given id. If no such tournament was found in the database, an "empty" tournament
     * object is returned.
     */
    public Tournament getTournament(int tournamentId) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM " + DATABASE + ".`tournament` WHERE " +
                    "`tournament_id` = " + tournamentId, tournamentRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotInDatabaseException("Couldn't find tournament in the database");
        }
    }

    /**
     * Finds the tournament with the given admin uuid.
     *
     * @param adminUUID the adminUUID of the tournament to be found
     * @return The tournament with the given UUID. If no such tournament was found in the database, an "empty" tournament
     * object is returned.
     */
    public Tournament getTournament(String adminUUID) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM " + DATABASE + " .`tournament` WHERE " +
                    "`admin_uuid` = \"" + adminUUID + "\"", tournamentRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotInDatabaseException("AdminUUID is incorrect");
        }
    }

    public List<Integer> getAvailableTables(int tournamentId) {
        return jdbcTemplate.queryForList("CALL get_available_tables(" + tournamentId + ")", Integer.class);
    }

    public void activate(int tournamentId) {
        try {
            jdbcTemplate.update("UPDATE " + DATABASE + ".tournament SET `active` = 1, `finished` = 0" +
                    " WHERE tournament_id = " + tournamentId);
        } catch (DataAccessException e) {
            throw new TroubleUpdatingDBException("Could not activate tournament");
        }
    }

    public void inactivate(int tournamentId) {
        try {
            jdbcTemplate.update("UPDATE " + DATABASE + ".tournament SET `active` = 0 WHERE tournament_id = " + tournamentId);
        } catch (DataAccessException e) {
            throw new TroubleUpdatingDBException("Could not pause tournament");
        }
    }

    public void setInactive(int tournamentId){
        try {
            jdbcTemplate.update("UPDATE " + DATABASE + ".tournament SET `active` = 0 WHERE tournament_id = " + tournamentId);
        } catch (DataAccessException e) {
            throw new TroubleUpdatingDBException("Could not pause tournament");
        }
    }

    public boolean exists(int tournamentId) {
        try {
            getTournament(tournamentId);
            return true;
        } catch (NotInDatabaseException e) {
            return false;
        }
    }

    public boolean exists(String adminUUID) {
        try {
            getTournament(adminUUID);
            return true;
        } catch (NotInDatabaseException e) {
            return false;
        }
    }

    public boolean isActive(int tournamentId) {
        try {
            return jdbcTemplate.queryForObject("SELECT `active` FROM " + DATABASE + ".tournament WHERE tournament_id = " +
                    tournamentId, Boolean.class);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotInDatabaseException("Could not find tournament in the database");
        }
    }

    public void setStartTime(String time, int tournamentId) {
        try {
            jdbcTemplate.update("UPDATE " + DATABASE + ".tournament SET `start` = ? WHERE tournament_id = ?",
                    new Object[]{time, tournamentId});
        } catch (DataAccessException e) {
            throw new TroubleUpdatingDBException("Could not set start time");
        }
    }

    public void setEndTime(String time, int tournamentId) {
        try {
            jdbcTemplate.update("UPDATE " + DATABASE + ".tournament SET `end` = ? WHERE tournament_id = ?",
                    new Object[]{time, tournamentId});
        } catch (DataAccessException e) {
            throw new TroubleUpdatingDBException("Could not set end time");
        }
    }

    public void finishTournament(int tournamentId){
        try {
            jdbcTemplate.update("UPDATE " + DATABASE + ".tournament SET `finished` = 1 WHERE tournament_id = ?",
                    new Object[]{tournamentId});
        } catch (DataAccessException e) {
            throw new TroubleUpdatingDBException("Could not finish tournament");
        }
    }
}

