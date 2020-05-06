package no.ntnu.sjakkarena.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * A collection of icons
 */
public class PlayerIcons {

    private static ArrayList<String> ICONS = new ArrayList<String>(Arrays.asList("fas fa-chess-rook fa-3x",
            "fas fa-chess-queen fa-3x", "fas fa-chess-pawn fa-3x", "fas fa-chess-knight fa-3x", "fas fa-chess-king fa-3x",
            "fas fa-chess-bishop fa-3x"));

    /**
     * Returns a string representation of a random fontawesome icon
     *
     * @return a string representation of a random fontawesome icon
     */
    public static String getRandomIcon() {
        Random random = new Random();
        return ICONS.get(random.nextInt(ICONS.size()));
    }
}
