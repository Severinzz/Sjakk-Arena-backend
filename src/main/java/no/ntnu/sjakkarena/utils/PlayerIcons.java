package no.ntnu.sjakkarena.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PlayerIcons {

    private static ArrayList<String> ICONS = new ArrayList<String>(Arrays.asList("fas fa-chess-rook fa-3x",
            "fas fa-chess-queen fa-3x", "fas fa-chess-pawn fa-3x", "fas fa-chess-knight fa-3x", "fas fa-chess-king fa-3x",
            "fas fa-chess-bishop fa-3x"));

    /**
     * Returns a random fontawesome icon css class
     * @return a random fontawesome icon css class
     */
    public static String getRandomFontAwesomeIcon(){
        Random random = new Random();
        return ICONS.get(random.nextInt(ICONS.size()));
    }
}
