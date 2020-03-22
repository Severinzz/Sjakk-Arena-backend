package no.ntnu.sjakkarena.events;

import no.ntnu.sjakkarena.adaptedmonrad.AdaptedMonrad;
import org.springframework.context.ApplicationEvent;

public class TournamentStartedEvent extends ApplicationEvent {

    private int tournamentId;

    private AdaptedMonrad adaptedMonrad;

    public TournamentStartedEvent(Object source, int tournamentId, AdaptedMonrad adaptedMonrad) {
        super(source);
        this.tournamentId = tournamentId;
        this.adaptedMonrad = adaptedMonrad;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public AdaptedMonrad getAdaptedMonrad() {
        return adaptedMonrad;
    }
}
