package no.ntnu.sjakkarena.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class represents images uploaded to application.
 */
public class Image {
    private int imageId;
    @JsonAlias ("game")
    private int gameId;
    @JsonAlias ("player")
    private int playerId;
    private String timeUploaded;

    private Image(int imageId, int gameId, int playerId, String timeUploaded) {
        this.imageId = imageId;
        this.gameId = gameId;
        this.playerId = playerId;
        this.timeUploaded = timeUploaded;
    }

    @JsonAlias("time_uploaded")
    public String getTimeUploaded() {
        return timeUploaded;
    }

    public void setTimeUploaded(String timeUploaded) {
        this.timeUploaded = timeUploaded;
    }

    @JsonProperty("player_id")
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @JsonProperty("game_id")
    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @JsonProperty("image_id")
    public int getId() {
        return imageId;
    }

    public void setId(int imageId) {
        this.imageId = imageId;
    }
}
