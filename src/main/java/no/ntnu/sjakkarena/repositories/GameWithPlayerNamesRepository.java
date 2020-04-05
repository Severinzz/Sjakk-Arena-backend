package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.GameWithPlayerNames;
import no.ntnu.sjakkarena.mappers.GameWithPlayerNamesRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class GameWithPlayerNamesRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<GameWithPlayerNames> gameWithPlayerNamesRowMapper = new GameWithPlayerNamesRowMapper();

    /**
     * Get games by tournament
     *
     * @param tournamentId The id of the tournament to which the games belong.
     * @return The games in the tournament
     */
    public Collection<GameWithPlayerNames> getGamesWithPlayerNames(int tournamentId) {
        return jdbcTemplate.query("SELECT `game`.*, `white`.`name` AS `white_player_name`, " +
                "`black`.`name` AS `black_player_name`" +
                " FROM `sjakkarena`.`game` AS `game`, `sjakkarena`.`player` AS white, `sjakkarena`.`player` AS `black` "+
                " WHERE `game`.`white_player` = `white`.`player_id` AND " +
                "`game`.`black_player` = `black`.`player_id` AND `white`.`tournament` = " + tournamentId +
                " ORDER BY `game`.`start` DESC", gameWithPlayerNamesRowMapper);
    }

    public List<GameWithPlayerNames> getActiveGames(int tournamentId) {
        return jdbcTemplate.query("SELECT `game`.*, `white`.`name` AS `white_player_name`, " +
                "`black`.`name` AS `black_player_name`" +
                " FROM `sjakkarena`.`game` AS `game`, `sjakkarena`.`player` AS white, `sjakkarena`.`player` AS `black` "+
                " WHERE `game`.`white_player` = `white`.`player_id` AND " +
                "`game`.`black_player` = `black`.`player_id` AND `white`.`tournament` = " + tournamentId +
                " AND `game`.`active` = " + 1 +
                " ORDER BY `game`.`start` DESC", gameWithPlayerNamesRowMapper);
    }


    public List<GameWithPlayerNames> getGamesWithInvalidResult(int tournamentId) {
        String sql = "\tSELECT `game`.*, " +
                "\t`white`.`name` AS `white_player_name`,\n" +
                "\t`black`.`name` AS `black_player_name`\n" +
                "\tFROM `sjakkarena`.`game` \n" +
                "\tAS `game`, `sjakkarena`.`player` \n" +
                "\tAS white, `sjakkarena`.`player` \n" +
                "\tAS `black`\n" +
                "\tWHERE (valid_result = 0)\n" +
                "\tAND `game`.`white_player` = `white`.`player_id` \n" +
                "\tAND `game`.`black_player` = `black`.`player_id` \n" +
                "\tAND `white`.`tournament` = " + tournamentId + "\n" +
                "ORDER BY`game`.`start` DESC";
        return jdbcTemplate.query(sql, gameWithPlayerNamesRowMapper);
    }

    public GameWithPlayerNames getActiveGame(int playerId) {
        return jdbcTemplate.queryForObject("SELECT `game`.*, `white`.`name` AS `white_player_name`, " +
                "`black`.`name` AS `black_player_name`" +
                " FROM `sjakkarena`.`game` AS `game`, `sjakkarena`.`player` AS white, `sjakkarena`.`player` AS `black`" +
                " WHERE `game`.`white_player` = `white`.`player_id` AND " +
                " `game`.`black_player` = `black`.`player_id`" +
                " AND (`white`.`player_id` = " + playerId + " OR `black`.`player_id` = " + playerId + ") AND `game`.`active` = " + 1 +
                " ORDER BY `game`.`start` DESC", gameWithPlayerNamesRowMapper);
    }

    public List<GameWithPlayerNames> getInActiveGames(int playerId) {
        return jdbcTemplate.query("SELECT `game`.*, `white`.`name` AS `white_player_name`, " +
                "`black`.`name` AS `black_player_name`" +
                " FROM `sjakkarena`.`game` AS `game`, `sjakkarena`.`player` AS white, `sjakkarena`.`player` AS `black`" +
                " WHERE `game`.`white_player` = `white`.`player_id` AND " +
                " `game`.`black_player` = `black`.`player_id`" +
                " AND (`white`.`player_id` = " + playerId + " OR `black`.`player_id` = " + playerId +  ") AND `game`.`active` = 0" +
                " ORDER BY `game`.`start` DESC", gameWithPlayerNamesRowMapper);
    }
}
