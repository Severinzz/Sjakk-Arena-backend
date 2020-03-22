package no.ntnu.sjakkarena.repositories;

import no.ntnu.sjakkarena.data.GameWithPlayerNames;
import no.ntnu.sjakkarena.mappers.GamesWithPlayerNamesRowMapper;
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

    private RowMapper<GameWithPlayerNames> gameTableElementRowMapper = new GamesWithPlayerNamesRowMapper();

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
                " ORDER BY `game`.`start` DESC", gameTableElementRowMapper);
    }

    public List<GameWithPlayerNames> getActiveGames(int tournamentId) {
        return jdbcTemplate.query("SELECT `game_id`, `table`, `white_player_points`, `game`.`active`, `game`.`start`, " +
                "`game`.`end`, `white_player` AS `white_player_id`, `white`.`name` AS `white_player_name`, " +
                "`black_player` AS `black_player_id`, `black`.`name` AS `black_player_name`" +
                " FROM `sjakkarena`.`game` AS `game`, `sjakkarena`.`player` AS white, `sjakkarena`.`player` AS `black`" +
                " WHERE `game`.`white_player` = `white`.`player_id` AND " +
                " `game`.`black_player` = `black`.`player_id`" +
                " AND `white`.`tournament` = " + tournamentId + " AND `game`.`active` = " + 1 +
                " ORDER BY `game`.`start` DESC", gameTableElementRowMapper);
    }
}

