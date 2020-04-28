package no.ntnu.sjakkarena.tasks;

import no.ntnu.sjakkarena.events.tournamentevents.TimeToEndTournamentEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * A task to end a tournament
 */
@Component
public class EndTournamentTask implements Runnable {

    private ApplicationEventPublisher applicationEventPublisher;

    private int tournamentId; // The tournament to end

    /**
     * Constructs a EncTournamentTask
     *
     * @param applicationEventPublisher Publisher of application events
     */
    public EndTournamentTask(ApplicationEventPublisher applicationEventPublisher){
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    /**
     * Publish a TimeToEncTournamentEvent
     */
    @Override
    public void run() {
        applicationEventPublisher.publishEvent(new TimeToEndTournamentEvent(this, tournamentId));
    }
}
