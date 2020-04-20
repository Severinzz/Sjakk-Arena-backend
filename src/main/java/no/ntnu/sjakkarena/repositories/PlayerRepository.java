package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.mappers.PlayerRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class PlayerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Player> playerRowMapper = new PlayerRowMapper();

    /**
     * Adds a new player to the database
     *
     * @param player the player to be added
     * @return the autogenerated player id
     */
    public int addNewPlayer(Player player) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            //Adapted code from https://stackoverflow.com/questions/12882874/how-can-i-get-the-autoincremented-id-when-i-insert-a-record-in-a-table-via-jdbct
            jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                            PreparedStatement ps =
                                    connection.prepareStatement("INSERT INTO sjakkarena.player (`player`.`name`, " +
                                                    "`player`.`tournament`, `player`.`icon`, `player`.`bib_number`) " +
                            "VALUES (\"" + player.getName()+ "\", " + player.getTournamentId() + ", \"" +
                            player.getIcon() + "\", get_random_bib_number(" + player.getTournamentId() + "))", new String[]{"id"});
                            return ps;
                        }
                    },
                    keyHolder);
            return keyHolder.getKey().intValue();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new TroubleUpdatingDBException("Could not add user to database. Possible reasons: \n" +
                    "1. User is already registered in database \n" +
                    "2. Name/value pairs in JSON are missing");
        }
    }

    /**
     * Returns a player
     *
     * @param playerId the id of the player to be returned
     * @return The player with the specified playerId
     */
    public Player getPlayer(int playerId){
        try {
            return jdbcTemplate.queryForObject("CALL sjakkarena.get_player(" + playerId + ")", playerRowMapper);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotInDatabaseException("Could not find player in the database");
        }
    }

    /**
     * Deletes a player from the database
     *
     * @param playerId
     */
    public void deletePlayer(int playerId) {
        int affectedRows = jdbcTemplate.update("DELETE FROM sjakkarena.player WHERE `player_id` = " + playerId);
        if (affectedRows != 1){
            throw new TroubleUpdatingDBException("Some problem occurred when trying to delete player");
        }
    }

    /**
     * Pause player
     * @param playerId id the of the player to pause.
     */
    public void pausePlayer(int playerId) {
        int affectedRows = jdbcTemplate.update("UPDATE `sjakkarena`.`player` SET paused = 1 WHERE player_id = " + playerId);
        if (affectedRows != 1){
            throw new TroubleUpdatingDBException("Some problem occurred when trying to pause player");
        }
    }

    /**
     * Unpause a player
     * @param playerId  The id of the player to unpause.
     */
    public void unpausePlayer(int playerId) {
        int affectedRows = jdbcTemplate.update("UPDATE sjakkarena.player SET paused = 0 WHERE player_id = " + playerId);
        if (affectedRows != 1){
            throw new TroubleUpdatingDBException("Some problem occurred when trying to unpause player");
        }
    }

    /**
     * Removes a user from the tournament he/she is enrolled in.
     * @param playerId The id of the player to be removed from the tournament
     */
    public void leaveTournament(int playerId) {
        int affectedRows = jdbcTemplate.update("UPDATE sjakkarena.player SET in_tournament = 0 WHERE player_id = " + playerId);
        if (affectedRows != 1) {
            throw new TroubleUpdatingDBException("Some problem occurred when trying to leave tournament");
        }
    }

    public List<Player> getPlayersInTournamentNotPlaying(int tournamentId) {
        List<Player> players = jdbcTemplate.query("CALL get_players_in_tournament_not_playing("+ tournamentId + ")",
                playerRowMapper);
        addPreviousOpponents(players);
        return players;
    }

    private void addPreviousOpponents(List<Player> players){
        for (Player player : players){
            addPreviousOpponents(player);
        }
    }

    private void addPreviousOpponents(Player player){
        List<Integer> previousOpponents =
                jdbcTemplate.queryForList("CALL get_previous_opponents("+ player.getId() + ")", Integer.class);
        player.setPreviousOpponents(previousOpponents);
    }

    /**
     * Returns the players enrolled in a tournament
     *
     * @param tournamentId the id of the tournament where the players are enrolled
     * @return A collection of players enrolled in a tournament
     */
    public List<Player> getPlayersInTournament(int tournamentId) {
        List<Player> players = jdbcTemplate.query("CALL get_players_in_tournament(" + tournamentId + ")", playerRowMapper);
        return players;
    }

    /**
     * Validate if player with name already exist in tournament
     * Returns true if there is one, false if not.
     * @param player object with a name and tournament id
     */
    public boolean doesPlayerExist(Player player) {
        // Adapted from: https://stackoverflow.com/questions/48546574/query-to-check-if-the-record-exists-in-spring-jdbc-template
        boolean exist = false;
        String sql = "SELECT * FROM sjakkarena.player WHERE player.name = ? AND player.tournament = ? LIMIT 1";
        List<Map<String, Object>> players = jdbcTemplate.queryForList(sql, new Object[] {player.getName(), player.getTournamentId()});
        if (players.size() != 0) {
            exist = true;
        }
        return exist;
    }

    /**
     * Returns the leaderboard of the given tournament
     * @param tournamentId The id of the tournament
     * @return A leaderboard of the given tournament
     */
    public List<Player> getLeaderBoard(int tournamentId) {
        List<Player> players = jdbcTemplate.query("CALL get_leader_board(" + tournamentId + ")", playerRowMapper);
        return players;
    }

    /**
     * Returns true if a there is a player with given ID and belongs to specified tournament ID.
     * Return false if not.
     * @param playerId ID of player
     * @param tournamentId ID of tournament
     * @return true if belongs, false if not.
     */
    public boolean playerBelongInTournament(int playerId, int tournamentId) {
        boolean belongInTournament = false;
        if(getPlayer(playerId).getTournamentId() == tournamentId) {
            belongInTournament = true;
        }
        return belongInTournament;
    }
}
