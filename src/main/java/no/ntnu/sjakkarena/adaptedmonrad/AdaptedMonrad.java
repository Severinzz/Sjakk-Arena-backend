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
        PlayerSorter.sortPlayersByAvgPointsAndBibNumber(playersNotPlaying);
        return setupGames(new ArrayDeque<>(playersNotPlaying), new PriorityQueue<>(availableTables));
    }

    protected abstract List<Game> setupGames(Deque<Player> playersNotPlaying, Queue<Integer> availableTables);

}
