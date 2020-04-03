package no.ntnu.sjakkarena.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Game {

    private int gameId;
    private int table;
    private String start;
    private String end;
    private int whitePlayerId;
    private int blackPlayerId;
    private Integer whitePlayerPoints;
    private boolean active;
    private boolean validResult;

    public Game(int table, String start, int whitePlayerId, int blackPlayerId, boolean active) {
        this.table = table;
        this.start = start;
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
        this.active = active;
    }

    public Game(int gameId, int table, String start, String end, int whitePlayerId, int blackPlayerId,
                Integer whitePlayerPoints, boolean active, boolean validResult) {
        if (active && whitePlayerPoints == 0){
            whitePlayerPoints = null;
        }
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
    public Integer getWhitePlayerPoints() {
        return whitePlayerPoints;
    }

    public void setWhitePlayerPoints(int whitePlayerPoints) {
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
