package no.ntnu.sjakkarena.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;

public class Player extends User{

    private String name;
    private boolean paused;
    private double points;
    private int rounds;
    @JsonAlias("tournament")
    private int tournamentId;
    private String icon;
    private boolean inTournament;
    private int bibNumber;
    private Collection<Integer> previousOpponents = new ArrayList<>();
    private int numberOfWhiteGames;
    private String lastPlayedColor;
    private int sameColorStreak;

    public Player() {
    }


    public Player(int playerId, String name, boolean paused, double points, int rounds, int tournamentId, String icon,
                  boolean inTournament, int numberOfWhiteGames, String lastPlayedColor, int sameColorStreak){
        super(playerId);
        if (lastPlayedColor == null){
            lastPlayedColor = "Ingen spilte parti";
        }
        this.name = name;
        this.paused = paused;
        this.points = points;
        this.rounds = rounds;
        this.tournamentId = tournamentId;
        this.icon = icon;
        this.inTournament = inTournament;
        this.numberOfWhiteGames = numberOfWhiteGames;
        this.lastPlayedColor = lastPlayedColor;
        this.sameColorStreak = sameColorStreak;
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

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    @JsonIgnore
    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    @JsonProperty("tournament_id")
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

    @JsonProperty("in_tournament")
    public boolean isInTournament() {
        return inTournament;
    }

    public void setInTournament(boolean inTournament) {
        this.inTournament = inTournament;
    }

    @JsonIgnore
    public int getBibNumber() {
        return bibNumber;
    }

    public void setBibNumber(int bibNumber) {
        this.bibNumber = bibNumber;
    }

    @JsonIgnore
    public Collection<Integer> getPreviousOpponents() {
        return previousOpponents;
    }

    public void setPreviousOpponents(Collection<Integer> previousOpponents) {
        this.previousOpponents = previousOpponents;
    }

    @JsonIgnore
    public int getNumberOfWhiteGames() {
        return numberOfWhiteGames;
    }

    public void setNumberOfWhiteGames(int numberOfWhiteGames) {
        this.numberOfWhiteGames = numberOfWhiteGames;
    }

    @JsonIgnore
    public String getLastPlayedColor() {
        return lastPlayedColor;
    }

    public void setLastPlayedColor(String lastPlayedColor) {
        this.lastPlayedColor = lastPlayedColor;
    }

    @JsonIgnore
    public int getSameColorStreak() {
        return sameColorStreak;
    }

    public void setSameColorStreak(int sameColorStreak) {
        this.sameColorStreak = sameColorStreak;
    }

    @JsonIgnore
    public double getWhiteGameRatio(){
        return (double) numberOfWhiteGames / rounds;
    }
}
