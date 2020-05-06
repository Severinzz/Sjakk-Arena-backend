package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.GameWithPlayerNames;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.mappers.GameWithPlayerNamesRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Repository containing methods for handling game and player data combined
 */
@Repository
public class GameWithPlayerNamesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<GameWithPlayerNames> gameWithPlayerNamesRowMapper = new GameWithPlayerNamesRowMapper();

    private static final String DATABASE = System.getenv("SJAKK_ARENA_DATABASE");



    /**
     * Returns games and player names associated with the specified tournament
     *
     * @param tournamentId The id of the tournament to which the games belong.
     * @return games and player names
     */
    public Collection<GameWithPlayerNames> getGamesWithPlayerNames(int tournamentId) {
        return jdbcTemplate.query("SELECT `game`.*, `white`.`name` AS `white_player_name`, " +
                "`black`.`name` AS `black_player_name`" +
                " FROM " + DATABASE + ".`game` AS `game`, " + DATABASE + ".`player` AS white, " + DATABASE + ".`player` AS `black` " +
                " WHERE `game`.`white_player` = `white`.`player_id` AND " +
                "`game`.`black_player` = `black`.`player_id` AND `white`.`tournament` = " + tournamentId +
                " ORDER BY `game`.`start` DESC", gameWithPlayerNamesRowMapper);
    }

    /**
     * Returns active games associated with the specified tournament
     *
     * @param tournamentId The id of the tournament to which the games belong
     * @return Active games associated with specified tournament
     */
    public List<GameWithPlayerNames> getActiveGames(int tournamentId) {
        return jdbcTemplate.query("SELECT `game`.*, `white`.`name` AS `white_player_name`, " +
                "`black`.`name` AS `black_player_name`" +
                " FROM " + DATABASE + ".`game` AS `game`, " + DATABASE + ".`player` AS white, " + DATABASE + ".`player` AS `black` " +
                " WHERE `game`.`white_player` = `white`.`player_id` AND " +
                "`game`.`black_player` = `black`.`player_id` AND `white`.`tournament` = " + tournamentId +
                " AND `game`.`active` = " + 1 +
                " ORDER BY `game`.`start` DESC", gameWithPlayerNamesRowMapper);
    }


    /**
     * Returns games with invalid results associated with the specified tournament
     *
     * @param tournamentId The id of the tournament to which the games belong
     * @return Games with invalid results associated with the specified tournament
     */
    public List<GameWithPlayerNames> getGamesWithInvalidResult(int tournamentId) {
        String sql = "\tSELECT `game`.*, " +
                "\t`white`.`name` AS `white_player_name`,\n" +
                "\t`black`.`name` AS `black_player_name`\n" +
                "\tFROM " + DATABASE + ".`game` \n" +
                "\tAS `game`, " + DATABASE + ".`player` \n" +
                "\tAS white, " + DATABASE + ".`player` \n" +
                "\tAS `black`\n" +
                "\tWHERE (valid_result = 0)\n" +
                "\tAND `game`.`white_player` = `white`.`player_id` \n" +
                "\tAND `game`.`black_player` = `black`.`player_id` \n" +
                "\tAND `white`.`tournament` = " + tournamentId + "\n" +
                "ORDER BY`game`.`start` DESC";
        return jdbcTemplate.query(sql, gameWithPlayerNamesRowMapper);
    }

    /**
     * Returns the specified player's active game
     *
     * @param playerId The id of the player
     * @return The specified player's active game
     */
    public GameWithPlayerNames getActiveGame(int playerId) {
        try {
            return jdbcTemplate.queryForObject("SELECT `game`.*, `white`.`name` AS `white_player_name`, " +
                    "`black`.`name` AS `black_player_name`" +
                    " FROM " + DATABASE + ".`game` AS `game`, " + DATABASE + ".`player` AS white, " + DATABASE + ".`player` AS `black`" +
                    " WHERE `game`.`white_player` = `white`.`player_id` AND " +
                    " `game`.`black_player` = `black`.`player_id`" +
                    " AND (`white`.`player_id` = " + playerId + " OR `black`.`player_id` = " + playerId + ") AND `game`.`active` = " + 1 +
                    " ORDER BY `game`.`start` DESC", gameWithPlayerNamesRowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw e;
        } catch (IncorrectResultSizeDataAccessException e) {
            throw e;
        }
    }

    /**
     * Returns the specified player's inactive games
     *
     * @param playerId The id of the player
     * @return The specified player's inactive games
     */
    public List<GameWithPlayerNames> getInActiveGames(int playerId) {
        try {
            return jdbcTemplate.query("SELECT `game`.*, `white`.`name` AS `white_player_name`, " +
                    "`black`.`name` AS `black_player_name`" +
                    " FROM " + DATABASE + ".`game` AS `game`, " + DATABASE + ".`player` AS white, " + DATABASE + ".`player` AS `black`" +
                    " WHERE `game`.`white_player` = `white`.`player_id` AND " +
                    " `game`.`black_player` = `black`.`player_id`" +
                    " AND (`white`.`player_id` = " + playerId + " OR `black`.`player_id` = " + playerId + ") AND `game`.`active` = 0" +
                    " ORDER BY `game`.`start` DESC", gameWithPlayerNamesRowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new NotInDatabaseException("Couldn't find player's inactive games");
        }
    }

    /**
     * Returns a game with the specified game id
     *
     * @param gameId The id of the game
     * @return A game with the specified game id
     */
    public GameWithPlayerNames getGame(int gameId) {
        return jdbcTemplate.queryForObject("SELECT `game`.*, `white`.`name` AS `white_player_name`, " +
                "`black`.`name` AS `black_player_name`" +
                " FROM " + DATABASE + ".`game` AS `game`, " + DATABASE + ".`player` AS white, " + DATABASE + ".`player` AS `black`" +
                " WHERE `game`.`white_player` = `white`.`player_id` AND " +
                " `game`.`black_player` = `black`.`player_id`" +
                " AND `game`.`game_id` = " + gameId, gameWithPlayerNamesRowMapper);
    }

    /**
     * Returns games with the specified ids
     *
     * @param ids The ids of the games to be returned
     * @return Games with the specified ids
     */
    public List<GameWithPlayerNames> getGames(List<Integer> ids){
        List<GameWithPlayerNames> gameWithPlayerNames = new ArrayList<>();
        for (Integer id : ids) {
            gameWithPlayerNames.add(getGame(id));
        }
        return gameWithPlayerNames;
    }
}
