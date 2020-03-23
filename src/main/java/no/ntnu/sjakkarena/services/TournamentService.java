package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.adaptedmonrad.AtTournamentStartAdaptedMonrad;
import no.ntnu.sjakkarena.events.TournamentStartedEvent;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.utils.RESTSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void startTournament() {
        tournamentRepository.setActive(RESTSession.getUserId());
        onTournamentStart();
    }

    private void onTournamentStart() {
        int tournamentId = RESTSession.getUserId();
        applicationEventPublisher.publishEvent(new TournamentStartedEvent(this, tournamentId));
    }
}
