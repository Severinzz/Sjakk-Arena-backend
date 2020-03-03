package no.ntnu.sjakkarena.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Player extends User{

    @Expose
    private String name;
    private boolean paused;
    private float points;
    private int rounds;

    @SerializedName("tournament")
    private int tournamentId;
    @Expose
    private String icon;

    @SerializedName("in_tournament")
    private boolean inTournament;

    public Player(int playerId, String name, boolean paused, float points, int rounds, int tournamentId, String icon,
                  boolean inTournament){
        super(playerId);
        this.name = name;
        this.paused = paused;
        this.points = points;
        this.rounds = rounds;
        this.tournamentId = tournamentId;
        this.icon = icon;
        this.inTournament = inTournament;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isInTournament() {
        return inTournament;
    }

    public void setInTournament(boolean inTournament) {
        this.inTournament = inTournament;
    }
}
