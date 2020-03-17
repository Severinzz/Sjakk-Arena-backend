package no.ntnu.sjakkarena.data;

public class Game {

    private int gameId;
    private int table;
    private String start;
    private String end;
    private int whitePlayerId;
    private int blackPlayerId;
    private String result;
    private boolean active;

    public Game(int table, String start, int whitePlayerId, int blackPlayerId) {
        this.table = table;
        this.start = start;
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
    }

    public Game(int gameId, int table, String start, String end, int whitePlayerId, int blackPlayerId, String result, boolean active) {
        this.gameId = gameId;
        this.table = table;
        this.start = start;
        this.end = end;
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
        this.result = result;
        this.active = active;
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

    public int getBlackPlayerId() {
        return blackPlayerId;
    }

    public void setBlackPlayerId(int blackPlayerId) {
        this.blackPlayerId = blackPlayerId;
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
}
