package no.ntnu.sjakkarena.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a player in a tournament. A player can only participate in a single tournament. This class extends
 * the User class.
 */
public class Player extends User {

    private String name;
    private boolean paused;
    private double points;
    private int rounds;
    @JsonAlias("tournament")
    private int tournamentId;
    private String icon;
    private boolean inTournament;
    private double bibNumber;
    private Collection<Integer> previousOpponents = new ArrayList<>();
    private int numberOfWhiteGames;
    private String lastPlayedColor;
    private int lastPlayedColorStreak;

    /**
     * Constructs a Player with the specified data
     *
     * @param playerId              The id of the player
     * @param name                  The name of the player
     * @param paused                Whether the player is taking a break
     * @param points                The points the player has received in a tournament
     * @param rounds                The number of rounds the player has played in a tournament
     * @param tournamentId          The id of the tournament the player is enrolled in
     * @param icon                  A representation of an icon
     * @param inTournament          Whether the player is participating in a tournament
     * @param numberOfWhiteGames    The number of games the player has played with white chessmen
     * @param lastPlayedColor       The color of the chessmen the player played with in his last game
     * @param lastPlayedColorStreak The number of games in a row the player has played with the lastPlayedColor
     * @param bibNumber             A bib number
     */
    public Player(int playerId, String name, boolean paused, double points, int rounds, int tournamentId, String icon,
                  boolean inTournament, int numberOfWhiteGames, String lastPlayedColor, int lastPlayedColorStreak,
                  double bibNumber) {
        super(playerId);
        if (lastPlayedColor == null) {
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
        this.lastPlayedColorStreak = lastPlayedColorStreak;
        this.bibNumber = bibNumber;
    }

    /**
     * Constructs a player with no data
     */
    public Player() {
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
    public double getBibNumber() {
        return bibNumber;
    }

    public void setBibNumber(double bibNumber) {
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
    public int getLastPlayedColorStreak() {
        return lastPlayedColorStreak;
    }

    public void setLastPlayedColorStreak(int lastPlayedColorStreak) {
        this.lastPlayedColorStreak = lastPlayedColorStreak;
    }

    @JsonIgnore
    public double getWhiteGameRatio() {
        return (double) numberOfWhiteGames / rounds;
    }
}
