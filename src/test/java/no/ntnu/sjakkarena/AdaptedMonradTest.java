package no.ntnu.sjakkarena;

import no.ntnu.sjakkarena.adaptedmonrad.AdaptedMonrad;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdaptedMonradTest {

    public List<Player> players;
    public List<Integer> availableTables;


    @Test
    public void provideBibNumber() {
        provideBibNumberInit();
        AdaptedMonrad.provideBibNumber(players);
        for (int i = 0; i < players.size(); i++) {
            assertEquals(players.get(i).getBibNumber(), i + 1);
        }
    }

    /**
     * Test that no games is created at the start of the tournament with no enrolled players.
     */
    @Test
    public void provideNewGamesAtTournamentStartNoEnrolledPlayers() {
        tablesInit(2);
        List<Game> games = AdaptedMonrad.provideNewGamesAtTournamentStart(new ArrayList<Player>(), availableTables);
        assertTrue(games.isEmpty());
    }

    @Test
    public void provideNewGamesAtTournamentStartEvenNumberOfPlayers(){
        playerInit(4);
        tablesInit(2);
        List<Game> newGames = AdaptedMonrad.provideNewGamesAtTournamentStart(players, availableTables);

        // First game
        Game newGame = newGames.get(0);
        assertEquals(newGame.getTable(), 1);
        assertTrue(newGame.getWhitePlayerId() == 2 && newGame.getBlackPlayerId() == 1);

        // Second game
        newGame = newGames.get(1);
        assertEquals(newGame.getTable(), 2);
        assertTrue(newGame.getWhitePlayerId() == 4 && newGame.getBlackPlayerId() == 3);
    }

    @Test
    public void provideNewGamesOnEarlyStart() {
        earlyStartInit();
        List<Game> newGames = AdaptedMonrad.provideNewGamesAtTournamentStart(players, availableTables);
        Game newGame = newGames.get(0);
        assertEquals(newGame.getTable(), 1);
        assertTrue(newGame.getWhitePlayerId() == 2 && newGame.getBlackPlayerId() == 1);
    }

    @Test
    public void provideNewGamesTwoPlayersWhoCannotMeetDueToSameColorStreak() {
        // One player
        earlyStartInit();
        twoPlayersWhoCannotMeetDueToSameColorStreakInit(players.get(0));
        List<Game> newGames = AdaptedMonrad.provideNewGames(players.subList(0, 2), availableTables);
        assertTrue(newGames.isEmpty());

        // both players
        twoPlayersWhoCannotMeetDueToSameColorStreakInit(players.get(1));
        newGames = AdaptedMonrad.provideNewGames(players.subList(0, 2), availableTables);
        assertTrue(newGames.isEmpty());
    }


    private void provideBibNumberInit() {
        players = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            players.add(new Player(i, "", true, 1, 1, 1, "",
                    true));
        }
    }

    /**
     * Player1 has played
     */
    private void twoPlayersWhoCannotMeetDueToSameColorStreakInit(Player player) {
        players.get(0).setSameColorStreak(3);
        players.get(0).setLastPlayedColor("white");
        players.get(0).setWhiteGames(3);
    }

    private void earlyStartInit(){
        playerInit(2);
        tablesInit(2);
    }

    private void playerInit(int numberOfPlayers) {
        players = new ArrayList<>();

        for (int i = 1; i <= numberOfPlayers; i++) {
            Player player = new Player(i, "Player" + i, true, 0.0, 0,
                    535937, "fa fa-chess-bishop", true);
            player.setWhiteGames(0);
            player.setLastPlayedColor("");
            player.setSameColorStreak(0);
            players.add(player);
        }
    }

    private void tablesInit(int numberOfTables) {
        availableTables = new ArrayList<>();
        for (int i = 1; i <= numberOfTables; i++) {
            availableTables.add(i);
        }
    }
}
