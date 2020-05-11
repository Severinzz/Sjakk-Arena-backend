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

import java.util.List;

/**
 * Repository containing methods for handling tournament data
 */
@Repository
public class TournamentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Tournament> tournamentRowMapper = new TournamentRowMapper();


    /**
     * Adds a new tournament to the database
     *
     * @param tournament The tournament to be added
     */
    public void addNewTournament(Tournament tournament) {
        String values = "";
        String attributes = "";
        if (tournament.getEnd() == null) {
            values = DBInteractionHelper.toValuesString(tournament.getId(),
                    tournament.getTournamentName(), tournament.getAdminEmail(), tournament.getStart(),
                    tournament.getTables(), tournament.getMaxRounds(), tournament.getHashedAdminUUID(),
                    tournament.isEarlyStart(), tournament.getSalt());
            attributes = "(`tournament_id`, `tournament_name`, `admin_email`, `start`, " +
                    "`tables`, `max_rounds`, `admin_uuid`, `early_start`, `salt`)";
        } else {
            values = DBInteractionHelper.toValuesString(tournament.getId(),
                    tournament.getTournamentName(), tournament.getAdminEmail(), tournament.getStart(), tournament.getEnd(),
                    tournament.getTables(), tournament.getMaxRounds(), tournament.getHashedAdminUUID(),
                    tournament.isEarlyStart(), tournament.getSalt());
            attributes = "(`tournament_id`, `tournament_name`, `admin_email`, `start`, `end`, " +
                    "`tables`, `max_rounds`, `admin_uuid`, `early_start`, `salt`)";
        }
        insertTournament(attributes, values);
    }

    /**
     * Inserts a tournament with the specified values
     *
     * @param attributes The attributes describing the tournament
     * @param values     The value of the tournament's attributes
     */
    private void insertTournament(String attributes, String values) {
        try {
            jdbcTemplate.update("INSERT INTO `sjakkarena`.`tournament` " +
                    attributes + " VALUES " + values);

        } catch (DataAccessException e) {
            throw new TroubleUpdatingDBException("Couldn't insert tournament into db");
        }
    }

    /**
     * Returns a tournament with the given id.
     *
     * @param tournamentId The id of the tournament to returned
     * @return A tournament with the given id. If no such tournament was found in the database.
     */
    public Tournament getTournament(int tournamentId) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM `sjakkarena`.`tournament` WHERE " +
                    "`tournament_id` = " + tournamentId, tournamentRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotInDatabaseException("Couldn't find tournament in the database");
        }
    }

    /**
     * Returns a tournament with the given admin uuid.
     *
     * @param adminUUID The adminUUID of the tournament to be returned
     * @return A tournament with the given UUID.
     */
    public Tournament getTournament(String adminUUID) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM `sjakkarena`.`tournament` WHERE " +
                    "`admin_uuid` = \"" + adminUUID + "\"", tournamentRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotInDatabaseException("AdminUUID is incorrect");
        }
    }

    /**
     * Returns the available tables in the specified tournament
     *
     * @param tournamentId The id of the tournament
     * @return The available tables in the specified tournament
     */
    public List<Integer> getAvailableTables(int tournamentId) {
        return jdbcTemplate.queryForList("CALL get_available_tables(" + tournamentId + ")", Integer.class);
    }

    /**
     * Activates the specified tournament
     *
     * @param tournamentId The id of the tournament to be activated
     */
    public void activate(int tournamentId) {
        try {
            jdbcTemplate.update("UPDATE sjakkarena.tournament SET `active` = 1, `finished` = 0" +
                    " WHERE tournament_id = " + tournamentId);
        } catch (DataAccessException e) {
            throw new TroubleUpdatingDBException("Could not activate tournament");
        }
    }

    /**
     * Inactivates the specified tournament
     *
     * @param tournamentId The id of the tournament to be inactivated
     */
    public void inactivate(int tournamentId) {
        try {
            jdbcTemplate.update("UPDATE sjakkarena.tournament SET `active` = 0 WHERE tournament_id = " + tournamentId);
        } catch (DataAccessException e) {
            throw new TroubleUpdatingDBException("Could not pause tournament");
        }
    }

    /**
     * Returns true if a tournament with the specified id exists
     *
     * @param tournamentId The id of the tournament
     * @return True if a tournament with the specified id exists
     */
    public boolean exists(int tournamentId) {
        try {
            getTournament(tournamentId);
            return true;
        } catch (NotInDatabaseException e) {
            return false;
        }
    }

    /**
     * Returns true if a tournament with the specified adminUUID exists
     *
     * @param adminUUID The adminUUID of the tournament
     * @return True if a tournament with the specified adminUUID exists
     */
    public boolean exists(String adminUUID) {
        try {
            getTournament(adminUUID);
            return true;
        } catch (NotInDatabaseException e) {
            return false;
        }
    }

    /**
     * Returns true if the specified tournament is active
     *
     * @param tournamentId The id of the tournament
     * @return True if the specified tournament is active
     */
    public boolean isActive(int tournamentId) {
        try {
            return jdbcTemplate.queryForObject("SELECT `active` FROM sjakkarena.tournament WHERE tournament_id = " +
                    tournamentId, Boolean.class);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotInDatabaseException("Could not find tournament in the database");
        }
    }

    /**
     * Sets the start time of the specified tournament
     *
     * @param time         The time of start of the tournament
     * @param tournamentId The id of the tournament
     */
    public void setStartTime(String time, int tournamentId) {
        try {
            jdbcTemplate.update("UPDATE sjakkarena.tournament SET `start` = ? WHERE tournament_id = ?",
                    new Object[]{time, tournamentId});
        } catch (DataAccessException e) {
            throw new TroubleUpdatingDBException("Could not set start time");
        }
    }

    /**
     * Sets the end time of the specified tournament
     *
     * @param time         The time of end of the tournament
     * @param tournamentId The id of the tournament
     */
    public void setEndTime(String time, int tournamentId) {
        try {
            jdbcTemplate.update("UPDATE sjakkarena.tournament SET `end` = ? WHERE tournament_id = ?",
                    new Object[]{time, tournamentId});
        } catch (DataAccessException e) {
            throw new TroubleUpdatingDBException("Could not set end time");
        }
    }

    /**
     * Finishes the specified tournament
     *
     * @param tournamentId The id of the tournament
     */
    public void finishTournament(int tournamentId) {
        try {
            jdbcTemplate.update("UPDATE sjakkarena.tournament SET `finished` = 1 WHERE tournament_id = ?",
                    new Object[]{tournamentId});
        } catch (DataAccessException e) {
            throw new TroubleUpdatingDBException("Could not finish tournament");
        }
    }

    /**
     * Returns all tournaments stored in the database
     *
     * @return all tournaments stored in the database
     */
    public List<Tournament> getAll() {
        return jdbcTemplate.query("SELECT * FROM sjakkarena.tournament", tournamentRowMapper);
    }
}

