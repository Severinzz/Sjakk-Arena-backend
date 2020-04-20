package no.ntnu.sjakkarena.AdaptedMonradTests;

import no.ntnu.sjakkarena.adaptedmonrad.AdaptedMonrad;
import no.ntnu.sjakkarena.adaptedmonrad.AtTournamentStartAdaptedMonrad;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.utils.PlayerSorter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AtTournamentStartAdaptedMonradTest extends AdaptedMonradTest{

    private AdaptedMonrad adaptedMonrad = new AtTournamentStartAdaptedMonrad();

    /**
     * Test that no games is created at the start of the tournament with no enrolled players.
     */
    @Test
    public void provideNewGamesAtTournamentStartNoEnrolledPlayers() {
        tablesInit(2);
        List<Game> games = adaptedMonrad.getNewGames(new ArrayList<Player>(), availableTables);
        assertTrue(games.isEmpty());
    }

    @Test
    public void provideNewGamesAtTournamentStartNotEnoughTables() {
        provideNewGamesAtTournamentStart(0, 4);
    }

    @Test
    public void provideNewGamesAtTournamentStartEvenNumberOfPlayers() {
        provideNewGamesAtTournamentStart(2, 4);
    }

    @Test
    public void provideNewGamesAtTournamentStartOddNumberOfPlayers() {
        provideNewGamesAtTournamentStart(2, 5);
    }


    private void provideNewGamesAtTournamentStart(int numberOfTables, int numberOfPlayers) {
        playerInit(numberOfPlayers);
        tablesInit(numberOfTables);
        List<Game> newGames = adaptedMonrad.getNewGames(players, availableTables);
        if (numberOfPlayers / 2 < numberOfTables) {
            assertEquals(numberOfPlayers / 2, newGames.size());
        } else {
            assertEquals(numberOfTables, newGames.size());
        }
        PlayerSorter.sortPlayersByAvgPointsAndBibNumber(players);
        for (int i = 0; i < newGames.size(); i++) {
            Game newGame = newGames.get(i);
            assertEquals(newGame.getTable(), i + 1);
            assertTrue(newGame.getWhitePlayerId() == players.get(2 * i + 1).getId() && newGame.getBlackPlayerId() == players.get(2 * i).getId());
        }
    }

    @Test
    public void provideNewGamesOnEarlyStart() {
        earlyStartInit();
        List<Game> games = adaptedMonrad.getNewGames(players, availableTables);
        Game game = games.get(0);
        assertEquals(game.getTable(), 1);
        PlayerSorter.sortPlayersByAvgPointsAndBibNumber(players);
        assertTrue(game.getWhitePlayerId() == players.get(1).getId() && game.getBlackPlayerId() == players.get(0).getId());
    }

    private void earlyStartInit() {
        playerInit(2);
        tablesInit(2);
    }

}
