package no.ntnu.sjakkarena.AdaptedMonradTests;

import no.ntnu.sjakkarena.adaptedmonrad.ColorDistribution;
import no.ntnu.sjakkarena.adaptedmonrad.ColorDistributor;
import no.ntnu.sjakkarena.data.Player;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for testing the ColorDistribution class
 */
public class ColorDistributorTest extends AdaptedMonradTest{

    /**
     * Test that when both players can play with both colors in the next round (according to the color rules) and
     * the players played their last games with opposite colors, they interchange colors.
     */
    @Test
    public void distributeWhenPlayersCanAlternateColor(){
        playerInit(2);

        Player whitePlayer = players.get(0);
        whitePlayer.setLastPlayedColor("white");

        Player blackPlayer = players.get(1);
        blackPlayer.setLastPlayedColor("black");
        ColorDistribution colorDistribution = ColorDistributor.distribute(whitePlayer, blackPlayer);

        assertEquals(blackPlayer, colorDistribution.getWhitePlayer());
        assertEquals(whitePlayer, colorDistribution.getBlackPlayer());
    }

    /**
     * Test that when both players can play with both colors in the next round (according to the color rules) and
     * the players played their last games with the same color, the player with the lowest ratio of white games to total
     * games played gets white.
     */
    @Test
    public void distributeWhiteToThePlayerWithLowestWhiteGamesRatio(){
        playerInit(2);

        Player lowestWhiteGameRatioPlayer = players.get(0);
        setStatsWithVariableNumberOfWhiteGames(lowestWhiteGameRatioPlayer, 1);

        Player highestWhiteGameRatioPlayer = players.get(1);
        setStatsWithVariableNumberOfWhiteGames(highestWhiteGameRatioPlayer, 3);

        ColorDistribution colorDistribution = ColorDistributor.distribute(highestWhiteGameRatioPlayer,
                lowestWhiteGameRatioPlayer);
        assertEquals(lowestWhiteGameRatioPlayer, colorDistribution.getWhitePlayer());
        assertEquals(highestWhiteGameRatioPlayer, colorDistribution.getBlackPlayer());
    }

    private void setStatsWithVariableNumberOfWhiteGames(Player player, int numberOfWhiteGames){
        player.setLastPlayedColor("white");
        player.setRounds(5);
        player.setNumberOfWhiteGames(numberOfWhiteGames);
    }
}
