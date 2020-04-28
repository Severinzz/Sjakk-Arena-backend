package no.ntnu.sjakkarena.tasks;

import no.ntnu.sjakkarena.events.tournamentevents.TimeToStartTournamentEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * A task to start a tournament
 */
@Component
public class StartTournamentTask implements Runnable {

    private ApplicationEventPublisher applicationEventPublisher;

    private int tournamentId; // The tournament to start

    /**
     * Constructs a StartTournamentTask
     *
     * @param applicationEventPublisher Publisher of application events
     */
    public StartTournamentTask(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    /**
     * Publish a TimeToStartTournamentEvent
     */
    @Override
    public void run() {
        applicationEventPublisher.publishEvent(new TimeToStartTournamentEvent(this, tournamentId));
    }
}
