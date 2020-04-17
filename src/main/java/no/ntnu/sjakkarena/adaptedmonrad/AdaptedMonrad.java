package no.ntnu.sjakkarena.adaptedmonrad;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.utils.PlayerSorter;

import java.util.*;

/**
 * This class is a start of the implementation of an adapted version of Monrad.
 *
 * More information about Monrad can be found here:
 * https://www.sjakk.no/kunnskap/systemer/norges-sjakkforbunds-monradsystem/
 *
 * To extend this class, the setupGames method has to be implemented.
 */
public abstract class AdaptedMonrad {

    /**
     * Gets new games in the tournament
     *
     * @param playersNotPlaying The players that is not currently playing a game
     * @param availableTables   The currently available tables
     * @return New games
     */
    public List<Game> getNewGames(List<Player> playersNotPlaying, Collection<Integer> availableTables) {
        PlayerSorter.sortPlayersByAvgPointsAndBibNumber(playersNotPlaying);
        return setupGames(new ArrayDeque<>(playersNotPlaying), new PriorityQueue<>(availableTables));
    }

    /**
     * Sets up new games. Includes finding opponents and seat the players at available tables.
     *
     * @param playersNotPlaying The players that is not currently playing a game
     * @param availableTables The currently available tables
     * @return New games
     */
    protected abstract List<Game> setupGames(Deque<Player> playersNotPlaying, Queue<Integer> availableTables);

}
