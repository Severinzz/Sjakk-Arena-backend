package no.ntnu.sjakkarena.AdaptedMonradTests;

import no.ntnu.sjakkarena.adaptedmonrad.AdaptedMonrad;
import no.ntnu.sjakkarena.adaptedmonrad.AfterTournamentStartAdaptedMonrad;
import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AfterTournamentStartAdaptedMonradTest extends AdaptedMonradTest {

    private AdaptedMonrad adaptedMonrad = new AfterTournamentStartAdaptedMonrad();

    @Test
    public void provideNewGamesTwoPlayersWhoCannotMeetDueToMaximumColorRatio(){
        colorRuleTestsInit(6, 2, 4);
        List<Game> games = adaptedMonrad.getNewGames(players, availableTables);
        assertTrue(games.isEmpty());
    }

    /**
     * Test the color streak rule
     */
    @Test
    public void provideNewGamesTwoPlayersWhoCannotMeetDueToSameColorStreak() {
        colorRuleTestsInit(5, 3, 3);
        List<Game> games = adaptedMonrad.getNewGames(players, availableTables);
        assertTrue(games.isEmpty());
    }


    private void colorRuleTestsInit(int rounds, int sameColorStreak, int whiteGames) {
        int numberOfPlayers = 2;
        playerInit(numberOfPlayers);
        tablesInit(2);
        for (int i = 0; i < numberOfPlayers; i++){
            setStats(players.get(i), 5, rounds, new ArrayList<>(), "white", sameColorStreak,
                    whiteGames);
        }
    }

    /**
     * Test the "have not played against each other" rule.
     */
    @Test
    public void provideNewGamesWhenOnlyTwoPlayerHasNotPlayedAgainstEachOther() {
        int numberOfPlayers = 9;
        onlyTwoPlayersHasNotPlayedAgainstEachOtherInit(numberOfPlayers);
        List<Game> games = adaptedMonrad.getNewGames(players, availableTables);
        assertEquals(1, games.size());

        Game game = games.get(0);
        assertTrue((game.getWhitePlayerId() == 1 && game.getBlackPlayerId() == numberOfPlayers) ||
                game.getWhitePlayerId() == numberOfPlayers && game.getBlackPlayerId() == 1);
    }


    private void onlyTwoPlayersHasNotPlayedAgainstEachOtherInit(int numberOfPlayers) {
        playerInit(numberOfPlayers);
        tablesInit(5);

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            for (int j = 0; j < players.size(); j++){
                if (j != i) {
                    player.getPreviousOpponents().add(players.get(j).getId());
                }
            }
        }
        Player highestRankedPlayer = players.get(0);
        Player lowestRankedPlayer = players.get(players.size() - 1);

        highestRankedPlayer.getPreviousOpponents().remove(lowestRankedPlayer.getId());
        lowestRankedPlayer.getPreviousOpponents().remove(highestRankedPlayer.getId());
    }

    /**
     * Test that the opponents that is chosen to play against each other are those who have the most similar score
     * and that can play opposite colors and haven't met each other in the tournament, so far.
     *
     * The player in first place cannot meet the player in second place since they have already played against each other.
     *
     * The player in first place cannot meet the player in third place since both players have a color streak of three
     * with the same color.
     *
     * The player in first place cannot meet the player in fourth place since both players have 50% + 1 av their
     * games played with the same color.
     *
     * The player in first place can meet all other players. According to the rules, the player in first place will then
     * meet the player in fifth place.
     */
    @Test
    public void provideNewGamesFindTheClosestPossibleOpponentToTheLeadingPlayer(){
        findTheClosestPossibleOpponentToTheLeadingPlayerInit();
        Player playerInFirstPlace = players.get(0);
        Player playerInFifthPlace = players.get(4);
        List<Game> games = adaptedMonrad.getNewGames(players, availableTables);
        GameInspector gameInspector = new GameInspector(games);
        assertTrue(gameInspector.isPlayingAgainstEachOther(playerInFirstPlace, playerInFifthPlace));
    }

    /**
     * The stats is not consistent. They are only chosen to comply with the test case scenario.
     *
     */
    private void findTheClosestPossibleOpponentToTheLeadingPlayerInit(){
        playerInit(6);
        tablesInit(3);

        //Player in first place
        Collection<Integer> previousOpponents = new ArrayList<>();
        previousOpponents.add(players.get(1).getId());
        setStats(players.get(0), 4, 4, previousOpponents, "white", 3, 3);

        //Player in second place
        previousOpponents = new ArrayList<>();
        previousOpponents.add(players.get(1).getId());
        setStats(players.get(1), 3, 4, previousOpponents, "black", 0, 0);

        //Player in third place
        setStats(players.get(2), 2, 4, new ArrayList<>(), "white", 3, 0);

        //Player in fourth place
        setStats(players.get(3), 1, 4, new ArrayList<>(), "black", 0, 3);

        //Player in fifth place
        setStats(players.get(4), 0.5, 4, new ArrayList<>(), "black", 0, 0);
    }

    private void setStats(Player player, double points, int rounds, Collection<Integer> previousOpponents,
                          String lastPlayedColor, int sameColorStreak, int whiteGames){
        player.setPoints(points);
        player.setRounds(rounds);
        player.setPreviousOpponents(previousOpponents);
        player.setLastPlayedColor(lastPlayedColor);
        player.setSameColorStreak(sameColorStreak);
        player.setNumberOfWhiteGames(whiteGames);
    }
}
