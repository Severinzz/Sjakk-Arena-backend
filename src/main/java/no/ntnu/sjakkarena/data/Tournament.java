package no.ntnu.sjakkarena.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * Represents a chess tournament. This class extends the user class.
 */
public class Tournament extends User {

    @JsonAlias("tournament_name")
    private String tournamentName;

    @Pattern(regexp = "^[A-Z\\WÆØÅ0-9._%+-]+@[A-Z\\WÆØÅ0-9.-]+\\.[A-Z\\WÆØÅ]{2,6}$", flags = Pattern.Flag.CASE_INSENSITIVE)
    // regex from https://stackoverflow.com/questions/8204680/java-regex-email
    @JsonAlias("admin_email")
    private String adminEmail;
    private String start;
    private String end;

    @Min(0)
    private int tables;

    @JsonAlias("max_rounds")
    @Min(0)
    private int maxRounds;
    private boolean active;

    @JsonAlias("admin_uuid")
    private String adminUUID;

    @JsonAlias("early_start")
    private boolean earlyStart;

    /**
     * Constructs a tournament with the specified data
     *
     * @param tournamentId   The id of the tournament
     * @param tournamentName The name of the tournament
     * @param adminEmail     The email of the tournament administrator
     * @param start          The point in time when the tournament starts
     * @param end            The point in time when the tournament ends
     * @param tables         The number of tables in the tournament
     * @param maxRounds      The maximum number of rounds in the tournament.
     * @param active         Whether the tournament is active
     * @param adminUUID      An unique id the admin can use to sign in
     * @param earlyStart     Whether to start the tournament when two players are enrolled
     */
    public Tournament(int tournamentId, String tournamentName, String adminEmail, String start, String end,
                      int tables, int maxRounds, boolean active, String adminUUID, boolean earlyStart) {
        super(tournamentId);
        this.tournamentName = tournamentName;
        this.adminEmail = adminEmail;
        this.start = start;
        this.end = end;
        this.tables = tables;
        this.maxRounds = maxRounds;
        this.active = active;
        this.adminUUID = adminUUID;
        this.earlyStart = earlyStart;
    }

    @JsonProperty("tournament_name")
    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    @JsonProperty("admin_email")
    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
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

    public int getTables() {
        return tables;
    }

    public void setTables(int tables) {
        this.tables = tables;
    }

    @JsonProperty("max_rounds")
    public int getMaxRounds() {
        return maxRounds;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @JsonIgnore
    public String getAdminUUID() {
        return adminUUID;
    }

    public void setAdminUUID(String adminUUID) {
        this.adminUUID = adminUUID;
    }

    @JsonProperty("early_start")
    public boolean isEarlyStart() {
        return earlyStart;
    }

    public void setEarlyStart(boolean earlyStart) {
        this.earlyStart = earlyStart;
    }
}
