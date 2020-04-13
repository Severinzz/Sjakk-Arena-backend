package no.ntnu.sjakkarena.utils;

import no.ntnu.sjakkarena.data.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlayerSorter {

    /**
     * Sorts a list of players. The higher the avg. score a player has, the smaller the player's index in the sorted list is.
     * Players with the same avg. score is sorted by their bib numbers. The smallest bib number is first among the
     * same avg. scored players in the sorted list.
     *
     * @param players The list to be sorted.
     */
    public static void sortPlayersByAvgPointsAndBibNumber(List<Player> players) {
        Collections.sort(players, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Double avgPoints1 = ((Player) o1).getRounds() == 0 ? 0 :
                        ((Player) o1).getPoints() / ((Player) o1).getRounds();
                Double avgPoints2 = ((Player) o2).getRounds() == 0 ? 0 :
                        ((Player) o2).getPoints() / ((Player) o2).getRounds();
                int comparison = avgPoints2.compareTo(avgPoints1);

                if (comparison == 0) {
                    Double bibNumber1 = ((Player) o1).getBibNumber();
                    Double bibNumber2 = ((Player) o2).getBibNumber();
                    comparison = bibNumber1.compareTo(bibNumber2);
                }
                return comparison;
            }
        });
    }

    /**
     * Sorts a list of players. The higher the avg. score a player has, the smaller the player's index in the sorted list is.
     * Players with the same avg. score is sorted by their bib numbers. The smallest bib number is first among the
     * same avg. scored players in the sorted list.
     *
     * @param players The list to be sorted.
     */
    public static void sortPlayersByPoints(List<Player> players) {
        Collections.sort(players, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Double points1 =  ((Player) o1).getPoints();
                Double points2 =  ((Player) o2).getPoints();
                return points2.compareTo(points1);
            }
        });
    }
}
