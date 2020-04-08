package no.ntnu.sjakkarena.events.tournamentevents;

import org.springframework.context.ApplicationEvent;

public class TournamentEndedEvent extends ApplicationEvent {


    private int tournamentId;

    public TournamentEndedEvent(Object source, int tournamentId) {
        super(source);
        this.tournamentId = tournamentId;
    }

    public int getTournamentId() {
        return tournamentId;
    }
}
