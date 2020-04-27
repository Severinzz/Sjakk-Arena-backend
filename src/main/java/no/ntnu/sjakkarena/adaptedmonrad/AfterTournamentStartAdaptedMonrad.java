package no.ntnu.sjakkarena.adaptedmonrad;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;

import java.time.LocalDateTime;
import java.util.*;


/**
 * An extension of the AdaptedMonrad class.
 *
 * This class handles game creation after the tournament has started. This involves applying rules regarding colors
 * and previous opponents.
 */
public class AfterTournamentStartAdaptedMonrad extends AdaptedMonrad {

    /**
     * Sets up new games. Includes finding opponents and seat them at available tables.
     *
     * @param playersNotPlaying The players that is not currently playing a game
     * @param availableTables   The currently available tables
     * @return New games
     */
    @Override
    protected List<Game> setupGames(Deque<Player> playersNotPlaying, Queue<Integer> availableTables) {
        List<Game> games = new ArrayList<>();
        while (!playersNotPlaying.isEmpty() && !availableTables.isEmpty()) {
            try {
                Player challenger = playersNotPlaying.pollFirst();
                Player opponent = findOpponent(challenger,
                        new ArrayList(playersNotPlaying));
                playersNotPlaying.remove(opponent);
                ChessmenColorDistribution chessmenColorDistribution = ChessmenColorDistributor.distribute(challenger, opponent);
                games.add(Game.notEnded(availableTables.poll(), LocalDateTime.now().withNano(0).toString(),
                        chessmenColorDistribution.getWhitePlayer().getId(), chessmenColorDistribution.getBlackPlayer().getId(),
                        true));
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
        }
        return games;
    }

    /**
     * Find an opponent to the challenger.
     *
     * @param challenger        The player to be given an opponent.
     * @param possibleOpponents Opponents that, if they meet some criteria, can play against the challenger in the next
     *                          game.
     * @return opponent An opponent to the challenger.
     */
    private Player findOpponent(Player challenger, List<Player> possibleOpponents) {
        int i = 0;
        boolean opponentIsNotFound = true;
        Player opponent = null;
        while (opponentIsNotFound && i < possibleOpponents.size()) {
            Player possibleOpponent = possibleOpponents.get(i);
            if (canMeet(challenger, possibleOpponent)) {
                opponent = possibleOpponent;
                opponentIsNotFound = false;
            }
            i++;
        }
        if (opponent != null) {
            return opponent;
        } else {
            throw new NoSuchElementException("No opponents found");
        }
    }

    /**
     * Test if two players can meet each other in the next round.
     *
     * @param player1 A player in a tournament
     * @param player2 A player in a tournament
     * @return True if player can meet each other in the next round, otherwise false
     */
    private boolean canMeet(Player player1, Player player2) {
        return hasNotPlayedAgainstEachOther(player1, player2)
                && ChessmenColorDistributor.canDistributeColors(player1, player2);
    }

    /**
     * Test if two players have played against each other earlier in the tournament
     *
     * @param player1 A player in a tournament
     * @param player2 A player in a tournament
     * @return True if the players don't have played against each other earlier in the tournament, otherwise false
     */
    private boolean hasNotPlayedAgainstEachOther(Player player1, Player player2) {
        boolean hasNotPlayed = true;
        for (Integer playerId : player1.getPreviousOpponents()) {
            if (player2.getId() == playerId) {
                hasNotPlayed = false;
            }
        }
        return hasNotPlayed;
    }
}
