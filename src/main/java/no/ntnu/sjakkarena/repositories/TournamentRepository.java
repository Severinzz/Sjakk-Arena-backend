package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;

import no.ntnu.sjakkarena.mappers.PlayerRowMapper;
import no.ntnu.sjakkarena.utils.DBInteractionHelper;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.mappers.TournamentRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Repository containing methods for handling tournament data
 */
@Repository
public class TournamentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Tournament> tournamentRowMapper = new TournamentRowMapper();

    private RowMapper<Player> playerRowMapper = new PlayerRowMapper();

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
            jdbcTemplate.update("INSERT INTO `sjakkarena`.`tournament` " +
                    attributes + " VALUES " + values);

        } catch (DataAccessException e) {
            throw new NotAbleToUpdateDBException("Couldn't insert tournament into db");
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
            return jdbcTemplate.queryForObject("SELECT * FROM `sjakkarena`.`tournament` WHERE " +
                    "`tournament_id` = " + tournamentId, tournamentRowMapper);
        } catch (NullPointerException e) {
            return new Tournament();
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
            return jdbcTemplate.queryForObject("SELECT * FROM `sjakkarena`.`tournament` WHERE " +
                    "`admin_uuid` = \"" + adminUUID + "\"", tournamentRowMapper);
        } catch (NullPointerException | EmptyResultDataAccessException e) {
            return new Tournament();
        }
    }

    /**
     * Returns the players enrolled in a tournament
     *
     * @param tournamentId the id of the tournament where the players are enrolled
     * @return A collection of players enrolled in a tournament
     */
    public Collection<Player> getPlayers(int tournamentId) {
        List<Player> players = jdbcTemplate.query("SELECT * FROM  `sjakkarena`.`player` WHERE " +
                "`in_tournament` = 1 AND `tournament` = " + tournamentId, playerRowMapper);
        return players;
    }



    /**
     * Returns the leaderboard of the given tournament
     * @param tournamentId The id of the tournament
     * @return A leaderboard of the given tournament
     */
    public Collection<Player> getPlayersSortedByPoints(int tournamentId) {
        List<Player> players = jdbcTemplate.query("SELECT * FROM  `sjakkarena`.`player` WHERE " +
                "`in_tournament` = 1 AND `tournament` = " + tournamentId + " ORDER BY `points` DESC", playerRowMapper);
        return players;
    }

    public List<Integer> getAvailableTables(int tournamentId) {
        return jdbcTemplate.queryForList("CALL get_available_tables(" + tournamentId + ")", Integer.class);
    }

    public void setActive(int tournamentId) {
        jdbcTemplate.update("UPDATE sjakkarena.tournament SET `active` = 1 WHERE tournament_id = " + tournamentId);
    }
}

