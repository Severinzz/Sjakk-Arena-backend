package no.ntnu.sjakkarena.adaptedmonrad;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;

import java.time.LocalDateTime;
import java.util.*;


/**
 * An extension of the AdaptedMonrad class.
 *
 * This class handles game creation when the tournament starts.
 */
public class AtTournamentStartAdaptedMonrad extends AdaptedMonrad{

    /**
     * Sets up new games. Includes finding opponents and seat the players at available tables.
     *
     * @param playersNotPlaying The players that is not currently playing a game
     * @param availableTables The currently available tables
     * @return New games
     */
    @Override
    protected List<Game> setupGames(Deque<Player> playersNotPlaying, Queue<Integer> availableTables) {
        List<Player> players = new ArrayList<>(playersNotPlaying);
        List<Game> games = new ArrayList<>();
        int i = 0;
        while ((i + 1 < playersNotPlaying.size()) && !availableTables.isEmpty()) {
            games.add(new Game(availableTables.poll(), LocalDateTime.now().withNano(0).toString(),
                    players.get(i+1).getId(), players.get(i).getId(), true));
            i += 2;
        }
        return games;
    }
}
