package no.ntnu.sjakkarena.adaptedmonrad;

import no.ntnu.sjakkarena.data.Player;

/**
 * A class describing which player plays with white chessmen and which player plays with black chessmen
 */
public class ChessmenColorDistribution {

    private Player whitePlayer;
    private Player blackPlayer;

    /**
     * Constructs a ChessmenColorDistribution
     *
     * @param whitePlayer The player playing with white chessmen
     * @param blackPlayer The player playing with black chessmen
     */
    public ChessmenColorDistribution(Player whitePlayer, Player blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public void setWhitePlayer(Player whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(Player blackPlayer) {
        this.blackPlayer = blackPlayer;
    }
}
