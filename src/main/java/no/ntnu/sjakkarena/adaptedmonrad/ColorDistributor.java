package no.ntnu.sjakkarena.adaptedmonrad;

import no.ntnu.sjakkarena.data.Player;

public class ColorDistributor {

    /**
     * Test if colors can be distributed to the two competing players in the next round
     *
     * @param player1
     * @param player2
     * @return True if colors can be distributed to the two competing players in the next round. Otherwise false.
     */
    public static boolean canDistributeColors(Player player1, Player player2) {
        return (ColorRules.canPlayWhite(player1) && ColorRules.canPlayBlack(player2)) ||
                (ColorRules.canPlayBlack(player1) && ColorRules.canPlayWhite(player2));
    }

    /**
     * Choose color for each of the competing players.
     *
     * @param player1
     * @param player2
     * @return A color distribution telling which player who is going to play with what color.
     */
    public static ColorDistribution distribute(Player player1, Player player2) {
        ColorDistribution colorDistribution;
        // If both players can play with any color.
        if (ColorRules.canPlayWhite(player1) && ColorRules.canPlayWhite(player2) && ColorRules.canPlayBlack(player1) &&
                ColorRules.canPlayBlack(player2)) {
            colorDistribution = distributeWhenBothPlayersCanPlayWithBothColors(player1, player2);
        }
        // If both players cannot play with any color
        else if (ColorRules.canPlayWhite(player1) && ColorRules.canPlayBlack(player2)) {
            colorDistribution = new ColorDistribution(player1, player2);
        } else if (ColorRules.canPlayBlack(player1) && ColorRules.canPlayWhite(player2)) {
            colorDistribution = new ColorDistribution(player2, player1);
        } else {
            throw new Error("Colors cannot be distributed");
        }
        return colorDistribution;
    }

    /**
     * Distribute colors. Tries to let players play with different color than it their last games. If different colors
     * can't be provided, the player with the lowest ratio of number of white games to number of games played, gets white.
     *
     * @param player1
     * @param player2
     * @return A color distribution telling which player is going to play with what color.
     */
    private static ColorDistribution distributeWhenBothPlayersCanPlayWithBothColors(Player player1, Player player2) {
        ColorDistribution colorDistribution;
        if (player1.getLastPlayedColor().equals(player2.getLastPlayedColor())) {
            colorDistribution = distributeBasedOnWhiteGameRatio(player1, player2);
        } else {
            Player nextWhite = player1.getLastPlayedColor().equals("black") ? player1 : player2;
            Player nextBlack = player1.getLastPlayedColor().equals("white") ? player1 : player2;
            colorDistribution = new ColorDistribution(nextWhite, nextBlack);
        }
        return colorDistribution;
    }

    /**
     * Distribute colors. The player with the lowest number of white games to rounds played ratio gets to play white.
     *
     * @param player1
     * @param player2
     * @return A color distribution telling which player is going to play with what color.
     */
    private static ColorDistribution distributeBasedOnWhiteGameRatio(Player player1, Player player2) {
        ColorDistribution colorDistribution;
        if (player1.getWhiteGameRatio() <= player2.getWhiteGameRatio()) {
            colorDistribution = new ColorDistribution(player1, player2);
        } else {
            colorDistribution = new ColorDistribution(player2, player1);
        }
        return colorDistribution;
    }
}
