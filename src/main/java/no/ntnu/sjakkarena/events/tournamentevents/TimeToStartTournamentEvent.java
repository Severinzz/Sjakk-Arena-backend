package no.ntnu.sjakkarena.events.tournamentevents;

import org.springframework.context.ApplicationEvent;

public class TimeToStartTournamentEvent extends ApplicationEvent {

    private int tournamentId;

    public TimeToStartTournamentEvent(Object source, int tournamentId) {
        super(source);
        this.tournamentId = tournamentId;
    }

    public int getTournamentId() {
        return tournamentId;
    }
}
