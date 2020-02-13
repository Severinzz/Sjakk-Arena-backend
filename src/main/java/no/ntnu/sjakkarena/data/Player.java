package no.ntnu.sjakkarena.data;

public class Player extends User{

    private String name;
    private boolean active;
    private int points;
    private int rounds;
    private int tournament;

    public Player(int playerId, String name, boolean active, int points, int rounds, int tournament){
        super(playerId);
        this.name = name;
        this.active = active;
        this.points = points;
        this.rounds = rounds;
        this.tournament = tournament;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getTournament() {
        return tournament;
    }

    public void setTournament(int tournament) {
        this.tournament = tournament;
    }
}
