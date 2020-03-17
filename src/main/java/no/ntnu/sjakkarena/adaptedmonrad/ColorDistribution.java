package no.ntnu.sjakkarena.adaptedmonrad;

import no.ntnu.sjakkarena.data.Player;

import java.util.Random;

public class ColorDistribution {

    private int whitePlayer;
    private int blackPlayer;

    public ColorDistribution(int whitePlayer, int blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    public int getWhitePlayer() {
        return whitePlayer;
    }

    public void setWhitePlayer(int whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public int getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(int blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    /**
     * Creates a random color distribution. The two players cannot be given the same color.
     *
     * @param player1 One player to be given a color
     * @param player2 Another player to be given a color
     * @return A random color distribution
     */
    public static ColorDistribution getRandomColorDistribution(Player player1, Player player2) {
        Random random = new Random();
        ColorDistribution colorDistribution;
        if (random.nextInt(2) == 0) {
            colorDistribution = new ColorDistribution(player1.getId(), player2.getId());
        } else {
            colorDistribution = new ColorDistribution(player2.getId(), player1.getId());
        }
        return colorDistribution;
    }
}
