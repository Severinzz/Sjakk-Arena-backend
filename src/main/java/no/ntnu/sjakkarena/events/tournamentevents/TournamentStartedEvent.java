package no.ntnu.sjakkarena.events.tournamentevents;

import no.ntnu.sjakkarena.adaptedmonrad.AdaptedMonrad;
import no.ntnu.sjakkarena.adaptedmonrad.AtTournamentStartAdaptedMonrad;
import no.ntnu.sjakkarena.data.Player;
import org.springframework.context.ApplicationEvent;

import java.util.List;
public class TournamentStartedEvent extends ApplicationEvent {

    private int tournamentId;

    private List<Player> players;

    public TournamentStartedEvent(Object source, int tournamentId, List<Player> players) {
        super(source);
        this.tournamentId = tournamentId;
        this.players = players;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
