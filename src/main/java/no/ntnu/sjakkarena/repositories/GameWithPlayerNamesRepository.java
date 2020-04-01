package no.ntnu.sjakkarena.repositories;

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
        return jdbcTemplate.query("SELECT `game_id`, `table`, `white_player_points`, `game`.`active`, `game`.`start`, " +
                "`game`.`end`, `white_player` AS `white_player_id`, `white`.`name` AS `white_player_name`, " +
                "`black_player` AS `black_player_id`, `black`.`name` AS `black_player_name`" +
                " FROM `sjakkarena`.`game` AS `game`, `sjakkarena`.`player` AS white, `sjakkarena`.`player` AS `black`, " +
                " `sjakkarena`.`tournament` AS `tournament` " +
                " WHERE `game`.`white_player` = `white`.`player_id` AND " +
                "`game`.`black_player` = `black`.`player_id` AND `tournament`.`tournament_id` = `white`.`tournament`" +
                " AND `tournament`.`tournament_id` = " + tournamentId +
                " ORDER BY `game`.`start` DESC", gameWithPlayerNamesRowMapper);
    }

    public List<GameWithPlayerNames> getActiveGames(int tournamentId) {
        return jdbcTemplate.query("SELECT `game_id`, `table`, `white_player_points`, `game`.`active`, `game`.`start`, " +
                "`game`.`end`, `white_player` AS `white_player_id`, `white`.`name` AS `white_player_name`, " +
                "`black_player` AS `black_player_id`, `black`.`name` AS `black_player_name`" +
                " FROM `sjakkarena`.`game` AS `game`, `sjakkarena`.`player` AS white, `sjakkarena`.`player` AS `black`" +
                " WHERE `game`.`white_player` = `white`.`player_id` AND " +
                " `game`.`black_player` = `black`.`player_id`" +
                " AND `white`.`tournament` = " + tournamentId + " AND `game`.`active` = " + 1 +
                " ORDER BY `game`.`start` DESC", gameWithPlayerNamesRowMapper);
    }

    public Collection<GameWithPlayerNames> getInvalidGamesWithPlayerNames(int TournamentID) {
        String sql = "\tSELECT `game_id`, `table`, `white_player_points`, \n" +
                "\t`white_player` AS `white_player_id`, \n" +
                "\t`white`.`name` AS `white_player_name`,\n" +
                "\t`black_player` AS `black_player_id`, \n" +
                "\t`black`.`name` AS `black_player_name`,\n" +
                "\t`game`.`start`, `game`.`end`, `game`.`active`\n" +
                "\tFROM `sjakkarena`.`game` \n" +
                "\tAS `game`, `sjakkarena`.`player` \n" +
                "\tAS white, `sjakkarena`.`player` \n" +
                "\tAS `black`,`sjakkarena`.`tournament` \n" +
                "\tAS `tournament`\n" +
                "\tWHERE (valid_result = 0)\n" +
                "\tAND `game`.`white_player` = `white`.`player_id` \n" +
                "\tAND `game`.`black_player` = `black`.`player_id` \n" +
                "\tAND `tournament`.`tournament_id` = `white`.`tournament`\n" +
                "\tAND `tournament`.`tournament_id` = " + TournamentID + "\n" +
                "ORDER BY`game`.`start` DESC";
        return jdbcTemplate.query(sql, gameWithPlayerNamesRowMapper);
    }
}

