package no.ntnu.sjakkarena.data;

import com.google.gson.annotations.SerializedName;

/**
 * An element in a game table
 */
public class GameWithPlayerNames extends Game{

    @SerializedName("white_player_name")
    private String whitePlayerName;

    @SerializedName("black_player_name")
    private String blackPlayerName;

    public GameWithPlayerNames(int gameId, int table, String start, String end, int whitePlayerId, int blackPlayerId,
                               Integer whitePlayerPoints,  boolean active, String whitePlayerName,  String blackPlayerName) {
        super(gameId, table, start, end, whitePlayerId, blackPlayerId, whitePlayerPoints, active);
        this.whitePlayerName = whitePlayerName;
        this.blackPlayerName = blackPlayerName;
    }

    public String getWhitePlayerName() {
        return whitePlayerName;
    }

    public void setWhitePlayerName(String whitePlayerName) {
        this.whitePlayerName = whitePlayerName;
    }

    public String getBlackPlayerName() {
        return blackPlayerName;
    }

    public void setBlackPlayerName(String blackPlayerName) {
        this.blackPlayerName = blackPlayerName;
    }
}
