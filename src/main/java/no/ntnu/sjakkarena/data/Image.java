package no.ntnu.sjakkarena.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.IOException;

/**
 * Class represents images uploaded to application.
 */
@Entity
public class Image {
    @javax.persistence.Id
    private Integer imageId;
    @JsonAlias ("game")
    @Column
    private int gameId;
    @JsonAlias ("player")
    @Column
    private int playerId;
    @Column
    private String timeUploaded;
    @Column
    private String filename;

    public Image(int imageId, int gameId, int playerId, String timeUploaded, String filename) {
        this.imageId = imageId;
        this.gameId = gameId;
        this.playerId = playerId;
        this.timeUploaded = timeUploaded;
        this.filename = filename;
    }

    public Image(String fileName, int gameId, int playerId, String timeUploaded) throws IOException {
        this.filename = fileName;
        // this.data = file.getBytes();
        this.gameId = gameId;
        this.playerId = playerId;
        this.timeUploaded = timeUploaded;
    }

    public Image() {
    }

    public String getFilename() { return filename; }

    public void setFileName(String fileName) { this.filename = fileName; }

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
