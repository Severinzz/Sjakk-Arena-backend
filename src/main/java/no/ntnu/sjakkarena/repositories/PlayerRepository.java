package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.exceptions.NotAbleToUpdateDBException;
import no.ntnu.sjakkarena.mappers.PlayerRowMapper;
import no.ntnu.sjakkarena.utils.DBInteractionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Repository
public class PlayerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Player> rowMapper = new PlayerRowMapper();

    /**
     * Adds a new player to the database
     *
     * @param player the player to be added
     * @return the autogenerated player id
     */
    public int addNewPlayer(Player player) {
        String values = DBInteractionHelper.toValuesString(player.getName(),
                player.getTournamentId(), player.getIcon());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            //code from https://stackoverflow.com/questions/12882874/how-can-i-get-the-autoincremented-id-when-i-insert-a-record-in-a-table-via-jdbct
            jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                            PreparedStatement ps =
                                    connection.prepareStatement("INSERT INTO `sjakkarena`.`player` (`name`, `tournament`, `icon`)" +
                                            " VALUES " + values, new String[]{"id"});
                            return ps;
                        }
                    },
                    keyHolder);
            return keyHolder.getKey().intValue();
        } catch (DataAccessException e) {
            throw new NotAbleToUpdateDBException("Could not add user to database. Possible reasons: \n" +
                    "1. User is already registered in database \n" +
                    "2. Name/value pairs in JSON are missing");
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
                "`in_tournament` = 1 AND `tournament` = " + tournamentId, rowMapper);
        return players;
    }

    /**
     * Returns a player
     *
     * @param playerId the id of the player to be returned
     * @return The player with the specified playerId
     */
    public Player getPlayer(int playerId){
        return jdbcTemplate.queryForObject("SELECT * FROM  `sjakkarena`.`player` WHERE " +
                "`player_id` = " + playerId, rowMapper);
    }

    /**
     * Deletes a player from the database
     *
     * @param id
     */
    public void deletePlayer(int id) {
        try {
            jdbcTemplate.update("DELETE FROM player WHERE `player_id` = " + id);
        } catch (DataAccessException e) {
            throw new NotAbleToUpdateDBException("Couldn't delete player from database");
        }
    }

    /**
     * Returns the leaderboard of the given tournament
     * @param tournamentId The id of the tournament
     * @return A leaderboard of the given tournament
     */
    public Collection<Player> getPlayersInTournamentSortedByPoints(int tournamentId) {
        List<Player> players = jdbcTemplate.query("SELECT * FROM  `sjakkarena`.`player` WHERE " +
                "`in_tournament` = 1 AND `tournament` = " + tournamentId + " ORDER BY `points` DESC", rowMapper);
        return players;
    }

    /**
     * Pause player
     * @param playerId id the of the player to pause.
     */
    public void pausePlayer(int playerId) {
        String updateQuery = "UPDATE `sjakkarena`.`player` SET paused = 1 WHERE player_id = " + playerId;
        try {
            jdbcTemplate.update(updateQuery);
        }
        catch(DataAccessException e){
            throw new NotAbleToUpdateDBException("Could not set 'paused' field to 1");
        }
    }

    /**
     * Set player field 'paused' to 0.
     * Code adapted from: https://www.tutorialspoint.com/springjdbc/springjdbc_update_query.htm
     * @param id for the player to change value for.
     */
    public void unpausePlayer(int id) {
        String updateQuery = "UPDATE sjakkarena.player SET paused = 0 WHERE player_id = " + id;
        try {
            jdbcTemplate.update(updateQuery);
        }
        catch(DataAccessException e){
            throw new NotAbleToUpdateDBException("Could not set 'pause' field to 0");
        }
    }

    /**
     * Set player field 'active' to 0.
     * Code adapted from: https://www.tutorialspoint.com/springjdbc/springjdbc_update_query.htm
     * @param id for the player to change value for.
     */
    public void disablePlayer(int id) {
        String updateQuery = "UPDATE sjakkarena.player SET active = 0 WHERE player_id = " + id;
        try {
            jdbcTemplate.update(updateQuery);
        }
        catch(DataAccessException e){
            throw new NotAbleToUpdateDBException("Could not set 'active' field to 0");
        }
    }
}
