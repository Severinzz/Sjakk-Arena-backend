package no.ntnu.sjakkarena.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a game of chess in a tournament. This class extends the Game class and contains information about
 * the name of the players playing the game.
 */
public class GameWithPlayerNames extends Game {

    private String whitePlayerName;

    private String blackPlayerName;


    /**
     * Constructs a GamWithPlayerNames object
     *
     * @param gameId            The id of the game
     * @param table             The table where the game is played
     * @param start             The point of time the game started
     * @param end               The point of time the game ended
     * @param whitePlayerId     The id of the player playing with white chessmen
     * @param blackPlayerId     The id of the player playing with black chessmen
     * @param whitePlayerPoints The number of points the player playing with white chessmen got from the game
     * @param active            Whether the players are currently playing the game
     * @param whitePlayerName   The name of the player playing with white chessmen
     * @param blackPlayerName   The name of the player playing with black chessmen
     * @param validResult       Whether the result is regarded as valid
     */
    public GameWithPlayerNames(int gameId, int table, String start, String end, int whitePlayerId, int blackPlayerId,
                               Integer whitePlayerPoints, boolean active, String whitePlayerName, String blackPlayerName,
                               boolean validResult) {
        super(gameId, table, start, end, whitePlayerId, blackPlayerId, whitePlayerPoints, active, validResult);
        this.whitePlayerName = whitePlayerName;
        this.blackPlayerName = blackPlayerName;
    }

    @JsonProperty("white_player_name")
    public String getWhitePlayerName() {
        return whitePlayerName;
    }

    public void setWhitePlayerName(String whitePlayerName) {
        this.whitePlayerName = whitePlayerName;
    }

    @JsonProperty("black_player_name")
    public String getBlackPlayerName() {
        return blackPlayerName;
    }

    public void setBlackPlayerName(String blackPlayerName) {
        this.blackPlayerName = blackPlayerName;
    }
}
