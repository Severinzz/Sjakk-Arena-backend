package no.ntnu.sjakkarena.data;

import com.google.gson.annotations.SerializedName;

/**
 * An element in a game table
 */
public class GameTableElement {

    @SerializedName("game_id")
    private int gameId;
    private int table;
    private String result;
    private boolean active;
    private String start;
    private String end;

    @SerializedName("white_player_id")
    private int whitePlayerId;

    @SerializedName("white_player_name")
    private String whitePlayerName;

    @SerializedName("black_player_id")
    private int blackPlayerId;

    @SerializedName("black_player_name")
    private String blackPlayerName;

    public GameTableElement(int gameId, int table, String result, boolean active, String start, String end, int whitePlayerId, String whitePlayerName, int blackPlayerId, String blackPlayerName) {
        this.gameId = gameId;
        this.table = table;
        this.result = result;
        this.active = active;
        this.start = start;
        this.end = end;
        this.whitePlayerId = whitePlayerId;
        this.whitePlayerName = whitePlayerName;
        this.blackPlayerId = blackPlayerId;
        this.blackPlayerName = blackPlayerName;
    }

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public int getWhitePlayerId() {
        return whitePlayerId;
    }

    public void setWhitePlayerId(int whitePlayerId) {
        this.whitePlayerId = whitePlayerId;
    }

    public String getWhitePlayerName() {
        return whitePlayerName;
    }

    public void setWhitePlayerName(String whitePlayerName) {
        this.whitePlayerName = whitePlayerName;
    }

    public int getBlackPlayerId() {
        return blackPlayerId;
    }

    public void setBlackPlayerId(int blackPlayerId) {
        this.blackPlayerId = blackPlayerId;
    }

    public String getBlackPlayerName() {
        return blackPlayerName;
    }

    public void setBlackPlayerName(String blackPlayerName) {
        this.blackPlayerName = blackPlayerName;
    }
}
