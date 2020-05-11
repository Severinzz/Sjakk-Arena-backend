package no.ntnu.sjakkarena.adaptedmonrad;

import no.ntnu.sjakkarena.data.Player;

/**
 * This class distributes chessmen colors to player
 */
public class ChessmenColorDistributor {

    /**
     * Tests if chessmen colors can be distributed between two players
     *
     * @param player1 A player in a tournament
     * @param player2 A player in a tournament
     * @return True if colors can be distributed between the two players. Otherwise false.
     */
    public static boolean canDistributeColors(Player player1, Player player2) {
        return (ColorRules.canPlayWhite(player1) && ColorRules.canPlayBlack(player2)) ||
                (ColorRules.canPlayBlack(player1) && ColorRules.canPlayWhite(player2));
    }

    /**
     * Distributes chessmen colors between two players
     *
     * @param player1 A player in a tournament
     * @param player2 A player in a tournament
     * @return A chessmen color distribution.
     */
    public static ChessmenColorDistribution distribute(Player player1, Player player2) {
        ChessmenColorDistribution chessmenColorDistribution;
        // If both players can play with any color.
        if (ColorRules.canPlayWhite(player1) && ColorRules.canPlayWhite(player2) && ColorRules.canPlayBlack(player1) &&
                ColorRules.canPlayBlack(player2)) {
            chessmenColorDistribution = distributeWhenBothPlayersCanPlayWithBothColors(player1, player2);
        }
        // If both players cannot play with any color
        else if (ColorRules.canPlayWhite(player1) && ColorRules.canPlayBlack(player2)) {
            chessmenColorDistribution = new ChessmenColorDistribution(player1, player2);
        } else if (ColorRules.canPlayBlack(player1) && ColorRules.canPlayWhite(player2)) {
            chessmenColorDistribution = new ChessmenColorDistribution(player2, player1);
        } else {
            throw new Error("Colors cannot be distributed");
        }
        return chessmenColorDistribution;
    }

    /**
     * Distributes chessmen colors. Tries to let players play with different color than in their last games.
     * If both players played with the same color in their last game, the player with the lowest
     * white games to games played ratio, gets white.
     *
     * @param player1 A player in a tournament
     * @param player2 A player in a tournament
     * @return A chessmen color distribution
     */
    private static ChessmenColorDistribution distributeWhenBothPlayersCanPlayWithBothColors(Player player1, Player player2) {
        ChessmenColorDistribution chessmenColorDistribution;
        if (player1.getLastPlayedColor().equals(player2.getLastPlayedColor())) {
            chessmenColorDistribution = distributeBasedOnWhiteGameRatio(player1, player2);
        } else {
            Player nextWhite = player1.getLastPlayedColor().equals("black") ? player1 : player2;
            Player nextBlack = nextWhite == player1 ? player2 : player1;
            chessmenColorDistribution = new ChessmenColorDistribution(nextWhite, nextBlack);
        }
        return chessmenColorDistribution;
    }

    /**
     * Distributes chessmen colors. The player with the lowest white games to games played ratio, gets white
     *
     * @param player1 A player in a tournament
     * @param player2 A player in a tournament
     * @return A chessmen color distribution.
     */
    private static ChessmenColorDistribution distributeBasedOnWhiteGameRatio(Player player1, Player player2) {
        ChessmenColorDistribution chessmenColorDistribution;
        if (player1.getWhiteGameRatio() <= player2.getWhiteGameRatio()) {
            chessmenColorDistribution = new ChessmenColorDistribution(player1, player2);
        } else {
            chessmenColorDistribution = new ChessmenColorDistribution(player2, player1);
        }
        return chessmenColorDistribution;
    }
}
