package no.ntnu.sjakkarena.tasks;

import no.ntnu.sjakkarena.events.TimeToStartTournamentEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class StartTournamentTask implements Runnable {

    private ApplicationEventPublisher applicationEventPublisher;

    private int tournamentId;

    public StartTournamentTask(ApplicationEventPublisher applicationEventPublisher){
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    @Override
    public void run() {
        applicationEventPublisher.publishEvent(new TimeToStartTournamentEvent(this, tournamentId));
    }
}
