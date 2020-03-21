package no.ntnu.sjakkarena.AdaptedMonradTests;

import no.ntnu.sjakkarena.data.Player;

import java.util.ArrayList;
import java.util.List;

public class AdaptedMonradTest {

    public List<Player> players;
    public List<Integer> availableTables;

    protected void playerInit(int numberOfPlayers) {
        players = new ArrayList<>();

        for (int i = 1; i <= numberOfPlayers; i++) {
            Player player = new Player(i, "Player" + i, true, 0, 0,
                    535937, "fa fa-chess-bishop", true, 0,
                    "", 0);
            players.add(player);
        }
    }

    protected void tablesInit(int numberOfTables) {
        availableTables = new ArrayList<>();
        for (int i = 1; i <= numberOfTables; i++) {
            availableTables.add(i);
        }
    }
}
