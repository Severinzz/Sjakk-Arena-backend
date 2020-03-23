package no.ntnu.sjakkarena.events;

import no.ntnu.sjakkarena.adaptedmonrad.AdaptedMonrad;
import no.ntnu.sjakkarena.adaptedmonrad.AfterTournamentStartAdaptedMonrad;
import org.springframework.context.ApplicationEvent;

public class NewPlayerAddedEvent extends ApplicationEvent {

    private int tournamentId;

    private AdaptedMonrad adaptedMonrad;

    public NewPlayerAddedEvent(Object object, int tournamentId) {
        super(object);
        this.tournamentId = tournamentId;
        this.adaptedMonrad = new AfterTournamentStartAdaptedMonrad();
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public AdaptedMonrad getAdaptedMonrad() {
        return adaptedMonrad;
    }
}
