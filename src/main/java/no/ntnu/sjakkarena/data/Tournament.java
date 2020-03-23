package no.ntnu.sjakkarena.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tournament extends User {

    @JsonAlias("tournament_name")
    private String tournamentName;

    @JsonAlias("admin_email")
    private String adminEmail; // the email of the tournament administrator
    private String start; // point in time when tournament starts
    private String end; // point in time when tournament ends
    private int tables; //number of tables in the tournament

    @JsonAlias("max_rounds")
    private int maxRounds; // maximum number of rounds in the tournament
    private boolean active; // true if tournament isn't paused

    @JsonAlias("admin_uuid")
    private String adminUUID; // an unique id given to the admin of tournament

    @JsonAlias("early_start")
    private boolean earlyStart; //Start when two or more players are enrolled

    public Tournament() {
        super();
    }

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
