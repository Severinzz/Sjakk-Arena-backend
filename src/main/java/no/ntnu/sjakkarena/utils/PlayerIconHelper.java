package no.ntnu.sjakkarena.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PlayerIconHelper {

    private static ArrayList<String> ICONS = new ArrayList<String>(Arrays.asList("fas fa-chess-rook",
            "fas fa-chess-queen", "fas fa-chess-pawn", "fas fa-chess-knight", "fas fa-chess-king",
            "fas fa-chess-bishop"));

    /**
     * Returns a random fontawesome icon css class
     * @return a random fontawesome icon css class
     */
    public static String getRandomFontAwesomeIcon(){
        Random random = new Random();
        return ICONS.get(random.nextInt(ICONS.size()));
    }
}
