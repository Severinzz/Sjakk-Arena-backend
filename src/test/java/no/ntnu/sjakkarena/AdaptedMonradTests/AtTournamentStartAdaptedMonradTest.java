package no.ntnu.sjakkarena.AdaptedMonradTests;

import no.ntnu.sjakkarena.adaptedmonrad.AdaptedMonrad;
import no.ntnu.sjakkarena.adaptedmonrad.AtTournamentStartAdaptedMonrad;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
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
        for (int i = 0; i < newGames.size(); i++) {
            Game newGame = newGames.get(i);
            assertEquals(newGame.getTable(), i + 1);
            assertTrue(newGame.getWhitePlayerId() == (2 * i + 2) && newGame.getBlackPlayerId() == (2 * i + 1));
        }
    }

    @Test
    public void provideNewGamesOnEarlyStart() {
        earlyStartInit();
        List<Game> games = adaptedMonrad.getNewGames(players, availableTables);
        Game game = games.get(0);
        assertEquals(game.getTable(), 1);
        assertTrue(game.getWhitePlayerId() == 2 && game.getBlackPlayerId() == 1);
    }

    private void earlyStartInit() {
        playerInit(2);
        tablesInit(2);
    }

}
