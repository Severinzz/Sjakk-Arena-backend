package no.ntnu.sjakkarena.adaptedmonrad;

import no.ntnu.sjakkarena.data.Game;
import no.ntnu.sjakkarena.data.Player;

import java.time.LocalDateTime;
import java.util.*;
import java.util.List;


/**
 * TODO describe rules here.
 */
public class AdaptedMonrad {

    /**
     * Gives each player a random bib number
     *
     * @param players The players to be given a random bib number.
     */
    public static void provideBibNumber(List<Player> players) {
        Collections.shuffle(players);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setBibNumber(i + 1);
        }
    }

    /**
     * Get new games in the tournament
     * @param playersNotPlaying The players that is not currently playing a game
     * @param availableTables The currently available tables
     * @return New games.
     */
    public static List<Game> provideNewGames(List<Player> playersNotPlaying, Collection<Integer> availableTables) {
        sortPlayers(playersNotPlaying);
        return setupGames(playersNotPlaying, new PriorityQueue<>(availableTables));
    }

    /**
     * At the start of the tournament, get new games
     * @param playersNotPlaying The players that is not currently playing a game
     * @param availableTables The currently available tables
     * @return New games
     */
    public static List<Game> provideNewGamesAtTournamentStart(List<Player> playersNotPlaying,
                                                              Collection<Integer> availableTables){
        sortPlayers(playersNotPlaying);
        return setupGamesAtTournamentStart(playersNotPlaying, new PriorityQueue<>(availableTables));
    }

    /**
     * Set up games at the beginning of the tournament.
     * @param playersNotPlaying The players that is not currently playing a game
     * @param availableTables The currently available tables
     * @return New games
     */
    private static List<Game> setupGamesAtTournamentStart(List<Player> playersNotPlaying,
                                                   Queue<Integer> availableTables) {
        List<Game> games = new ArrayList<>();
        int i = 0;
        while ((i + 1 < playersNotPlaying.size()) && !availableTables.isEmpty()) {
            games.add(new Game(availableTables.poll(), LocalDateTime.now().withNano(0).toString(),
                    playersNotPlaying.get(i+1).getId(), playersNotPlaying.get(i).getId()));
            i += 2;
        }
        return games;
    }
    /**
     * Set up games. Find opponents and give them an available table TODO find a way to remove players from list when they are matched.
     *
     * @param playersNotPlaying The players that is not currently playing a game
     * @param availableTables The currently available tables
     * @return New games
     */
    private static List<Game> setupGames(List<Player> playersNotPlaying, Queue<Integer> availableTables) {
        List<Game> games = new ArrayList<>();
        int i = 0;
        while (i < playersNotPlaying.size() && !availableTables.isEmpty()) {
            try {
                Player challenger = playersNotPlaying.get(i);
                Player opponent = findOpponent(challenger,
                        playersNotPlaying.subList(i + 1, playersNotPlaying.size()));
                playersNotPlaying.remove(opponent);
                playersNotPlaying.remove(i);
                ColorDistribution colorDistribution = chooseColor(challenger, opponent);
                games.add(new Game(availableTables.poll(), LocalDateTime.now().withNano(0).toString(),
                        colorDistribution.getWhitePlayer(), colorDistribution.getBlackPlayer()));
                i++;
            }
            catch(NoSuchElementException e){
                i++;
            }
        }
        return games;
    }

    /**
     * Choose color for each of the competing players.
     *
     * @param player1
     * @param player2
     * @return A color distribution telling which player is going to play with what color.
     */
    private static ColorDistribution chooseColor(Player player1, Player player2) {
        ColorDistribution colorDistribution;
        // If both players can play with any color.
        if (canPlayWhite(player1) && canPlayWhite(player2) && canPlayBlack(player1) && canPlayBlack(player2)){
            colorDistribution = ColorDistribution.getRandomColorDistribution(player1, player2);
        }
        // If both players cannot play with any color
        else if (canPlayWhite(player1) && canPlayBlack(player2)) {
            colorDistribution = new ColorDistribution(player1.getId(), player2.getId());
        } else if (canPlayBlack(player1) && canPlayWhite(player2)) {
            colorDistribution = new ColorDistribution(player2.getId(), player1.getId());
        } else {
            throw new Error("Colors cannot be distributed");
        }
        return colorDistribution;
    }


    /**
     * Find an opponent to the challenging player.
     *
     * @param challenger        The player to be given an opponent.
     * @param possibleOpponents Opponents that, if they meet some criteria, can play against the challenger in the next
     *                          game.
     * @return opponent An opponent to the challenger.
     */
    private static Player findOpponent(Player challenger, List<Player> possibleOpponents) {
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
     * @param player1
     * @param player2
     * @return True if player can meet each other in
     */
    private static boolean canMeet(Player player1, Player player2){
        return hasNotPlayedAgainstEachOther(player1, player2)
                && canDistributeColors(player1, player2);
    }

    /**
     * Test if colors can be distributed to the two competing players in the next round
     *
     * @param player1
     * @param player2
     * @return True if colors can be distributed to the two competing players in the next round. Otherwise false.
     */
    private static boolean canDistributeColors(Player player1, Player player2) {
        return (canPlayWhite(player1) && canPlayBlack(player2)) || (canPlayBlack(player1) && canPlayWhite(player2));
    }

    /**
     * Test if a player can play with white pieces in his/hers next game
     *
     * @param player
     * @return True if player can play with white pieces in his/hers next game. Otherwise false.
     */
    private static boolean canPlayWhite(Player player) {
        int sameColorStreak = player.getLastPlayedColor().equals("white") ? player.getSameColorStreak() : 0;
        return compliesWithColorRules(sameColorStreak, player.getRounds(), player.getWhiteGames());
    }

    /**
     * Test if a player can play with black pieces in his/hers next game
     *
     * @param player
     * @return True if player can play with black pieces in his/hers next game. Otherwise false.
     */
    private static boolean canPlayBlack(Player player) {
        int blackGames = player.getRounds() - player.getWhiteGames();
        int sameColorStreak = player.getLastPlayedColor().equals("black") ? player.getSameColorStreak() : 0;
        return compliesWithColorRules(sameColorStreak, player.getRounds(), blackGames);
    }

    /**
     * Test if a player with the provided statistics complies with the color rules if he/she plays the next game with the
     * color the statics is about.
     *
     * @param colorStreak    The number of games with the same color in a row.
     * @param rounds         The number of rounds a player has played in the tournament
     * @param gamesWithColor The number of games played with the same color as the colorStreak measures .
     * @return True if the player complies with the color rules if he/she plays the next game with the
     * color the statics is about. Otherwise false.
     */
    private static boolean compliesWithColorRules(int colorStreak, int rounds, int gamesWithColor) {
        return (colorStreak < 3) && ((float) rounds / 2 <= gamesWithColor);
    }

    /**
     * Test if two players have played against each other earlier in the tournament
     *
     * @param player1
     * @param player2
     * @return True if the players don't have played against each other earlier in the tournament
     */
    private static boolean hasNotPlayedAgainstEachOther(Player player1, Player player2) {
        boolean hasNotPlayed = true;
        for (Integer playerId : player1.getPreviousOpponents()) {
            if (player2.getId() == playerId) {
                hasNotPlayed = false;
            }
        }
        return hasNotPlayed;
    }

    /**
     * Sorts a list of players. The higher the avg. score a player has, the smaller the player's index in the sorted list is.
     * Players with the same avg. score is sorted by their bib numbers. The smallest bib number is first among the
     * same avg. scored players in the sorted list.
     *
     * @param players The list to be sorted.
     */
    private static void sortPlayers(List<Player> players) {
        Collections.sort(players, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Double avgPoints1 = ((Player) o1).getPoints() / ((Player) o1).getRounds();
                Double avgPoints2 = ((Player) o2).getPoints() / ((Player) o2).getRounds();
                int comparison = avgPoints2.compareTo(avgPoints1);

                if (comparison == 0) {
                    Integer bibNumber1 = ((Player) o1).getBibNumber();
                    Integer bibNumber2 = ((Player) o2).getBibNumber();
                    comparison = bibNumber1.compareTo(bibNumber2);
                }
                return comparison;
            }
        });
    }
}
