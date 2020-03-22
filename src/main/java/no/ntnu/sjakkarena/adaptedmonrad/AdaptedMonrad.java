package no.ntnu.sjakkarena.adaptedmonrad;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.utils.PlayerSorter;

import java.util.*;

public abstract class AdaptedMonrad {

    /**
     * Get new games in the tournament
     *
     * @param playersNotPlaying The players that is not currently playing a game
     * @param availableTables   The currently available tables
     * @return New games.
     */
    public List<Game> getNewGames(List<Player> playersNotPlaying, Collection<Integer> availableTables) {
        PlayerSorter.sortPlayers(playersNotPlaying);
        return setupGames(new ArrayDeque<>(playersNotPlaying), new PriorityQueue<>(availableTables));
    }

    /**
     * Gives each player a random bib number
     *
     * @param players The players to be given a random bib number.
     */
    public void provideBibNumber(List<Player> players) {
        Collections.shuffle(players);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setBibNumber(i + 1);
        }
    }

    protected abstract List<Game> setupGames(ArrayDeque<Player> playersNotPlaying, Queue<Integer> availableTables);

}
