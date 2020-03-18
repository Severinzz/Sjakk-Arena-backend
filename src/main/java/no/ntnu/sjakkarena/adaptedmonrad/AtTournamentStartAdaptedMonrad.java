package no.ntnu.sjakkarena.adaptedmonrad;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.utils.PlayerSorter;

import java.time.LocalDateTime;
import java.util.*;

public class AtTournamentStartAdaptedMonrad {
    /**
     * Gives each player a random bib number
     *
     * @param players The players to be given a random bib number.
     */
    public static void provideBibNumber(List<Player> players) {
        Collections.shuffle(players);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setBibNumber(i + 1);
        }
    }

    /**
     * At the start of the tournament, get new games
     * @param playersNotPlaying The players that is not currently playing a game
     * @param availableTables The currently available tables
     * @return New games
     */
    public static List<Game> provideNewGames(List<Player> playersNotPlaying,
                                                              Collection<Integer> availableTables){
        PlayerSorter.sortPlayers(playersNotPlaying);
        return setupGames(playersNotPlaying, new PriorityQueue<>(availableTables));
    }



    /**
     * Set up games at the beginning of the tournament.
     * @param playersNotPlaying The players that is not currently playing a game
     * @param availableTables The currently available tables
     * @return New games
     */
    private static List<Game> setupGames(List<Player> playersNotPlaying,
                                         Queue<Integer> availableTables) {
        List<Game> games = new ArrayList<>();
        int i = 0;
        while ((i + 1 < playersNotPlaying.size()) && !availableTables.isEmpty()) {
            games.add(new Game(availableTables.poll(), LocalDateTime.now().withNano(0).toString(),
                    playersNotPlaying.get(i+1).getId(), playersNotPlaying.get(i).getId()));
            i += 2;
        }
        return games;
    }
}
