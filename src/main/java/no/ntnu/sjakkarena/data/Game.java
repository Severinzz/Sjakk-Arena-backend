package no.ntnu.sjakkarena.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents a game of chess in a tournament
 */
public class Game {

    private int gameId;
    private int table;
    private String start;
    private String end;
    private int whitePlayerId;
    private int blackPlayerId;
    private double whitePlayerPoints;
    private boolean active;
    private boolean validResult;

    private Game(boolean active) {
        this.active = active;
    }

    private Game(int table, String start, int whitePlayerId, int blackPlayerId, boolean active) {
        this.table = table;
        this.start = start;
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
        this.active = active;
    }

    protected Game(int gameId, int table, String start, String end, int whitePlayerId, int blackPlayerId,
                   double whitePlayerPoints, boolean active, boolean validResult) {
        this.gameId = gameId;
        this.table = table;
        this.start = start;
        this.end = end;
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
        this.whitePlayerPoints = whitePlayerPoints;
        this.active = active;
        this.validResult = validResult;
    }

    /**
     * Returns a Game object with the information needed to represent a non-finished game.
     *
     * @param table         The table where the game is played
     * @param start         The point of time the game started
     * @param whitePlayerId The id of the player playing with white chessmen
     * @param blackPlayerId The id of the player playing with black chessmen
     * @return a Game object with the information needed to represent a non-finished game.
     */
    public static Game notEnded(int table, String start, int whitePlayerId, int blackPlayerId, boolean active) {
        return new Game(table, start, whitePlayerId, blackPlayerId, active);
    }

    /**
     * Returns a Game with the same fields/attributes as in the database.
     *
     * @param gameId            The id of the game
     * @param table             The table where the game is played
     * @param start             The point of time the game started
     * @param end               The point of time the game ended
     * @param whitePlayerId     The id of the player playing with white chessmen
     * @param blackPlayerId     The id of the player playing with black chessmen
     * @param whitePlayerPoints The number of points the player playing with white chessmen got from the game
     * @param active            Whether the players are currently playing the game
     * @param validResult       Whether the result is regarded as valid
     * @return a Game with the same fields/attributes as in the database.
     */
    public static Game asInDatabase(int gameId, int table, String start, String end, int whitePlayerId, int blackPlayerId,
                                    double whitePlayerPoints, boolean active, boolean validResult) {
        return new Game(gameId, table, start, end, whitePlayerId, blackPlayerId, whitePlayerPoints, active, validResult);
    }

    /**
     * Returns a Game object containing only the information that the game is inactive.
     *
     * @return a Game object containing only the information that the game is inactive.
     */
    public static Game emptyInactiveGame() {
        return new Game(false);
    }

    @JsonProperty("game_id")
    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @JsonProperty("white_player_id")
    public int getWhitePlayerId() {
        return whitePlayerId;
    }

    public void setWhitePlayerId(int whitePlayerId) {
        this.whitePlayerId = whitePlayerId;
    }

    @JsonProperty("black_player_id")
    public int getBlackPlayerId() {
        return blackPlayerId;
    }

    public void setBlackPlayerId(int blackPlayerId) {
        this.blackPlayerId = blackPlayerId;
    }

    @JsonProperty("white_player_points")
    public double getWhitePlayerPoints() {
        return whitePlayerPoints;
    }

    public void setWhitePlayerPoints(double whitePlayerPoints) {
        this.whitePlayerPoints = whitePlayerPoints;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isValidResult() {
        return validResult;
    }
}
