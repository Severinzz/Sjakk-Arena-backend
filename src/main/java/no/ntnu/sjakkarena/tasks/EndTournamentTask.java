package no.ntnu.sjakkarena.tasks;

import no.ntnu.sjakkarena.events.tournamentevents.TimeToEndTournamentEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EndTournamentTask implements Runnable {

    private ApplicationEventPublisher applicationEventPublisher;

    private int tournamentId;

    public EndTournamentTask(ApplicationEventPublisher applicationEventPublisher){
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    @Override
    public void run() {
        applicationEventPublisher.publishEvent(new TimeToEndTournamentEvent(this, tournamentId));
    }
}
