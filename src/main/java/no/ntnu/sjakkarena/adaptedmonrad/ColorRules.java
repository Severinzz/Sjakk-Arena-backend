package no.ntnu.sjakkarena.adaptedmonrad;

import no.ntnu.sjakkarena.data.Player;

/**
 * This class tests if a player complies with color rules
 */
public class ColorRules {

    /**
     * Test if a player with the provided statistics complies with the color rules.
     *
     * @param colorStreak    The number of games with the same color in a row.
     * @param rounds         The number of rounds a player has played in the tournament
     * @param gamesWithColor The number of games played with the same color as the colorStreak measures .
     * @return True if the player complies with the color rules. Otherwise false.
     */
    private static boolean compliesWithColorRules(int colorStreak, int rounds, int gamesWithColor) {
        return (colorStreak < 3) && (((float) rounds + 1)/ 2 >= gamesWithColor);
    }

    /**
     * Test if a player can play with white pieces in his/hers next game
     *
     * @param player
     * @return True if player can play with white pieces in his/hers next game. Otherwise false.
     */
    public static boolean canPlayWhite(Player player) {
        int whiteStreak = player.getLastPlayedColor().equals("white") ? player.getSameColorStreak() : 0;
        return compliesWithColorRules(whiteStreak, player.getRounds(), player.getNumberOfWhiteGames());
    }

    /**
     * Test if a player can play with black pieces in his/hers next game
     *
     * @param player
     * @return True if player can play with black pieces in his/hers next game. Otherwise false.
     */
    public static boolean canPlayBlack(Player player) {
        int blackGames = player.getRounds() - player.getNumberOfWhiteGames();
        int blackStreak = player.getLastPlayedColor().equals("black") ? player.getSameColorStreak() : 0;
        return compliesWithColorRules(blackStreak, player.getRounds(), blackGames);
    }
}
