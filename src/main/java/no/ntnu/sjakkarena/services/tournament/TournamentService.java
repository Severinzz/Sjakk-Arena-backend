package no.ntnu.sjakkarena.services.tournament;

import no.ntnu.sjakkarena.GameCreator;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.eventcreators.TournamentEventCreator;
import no.ntnu.sjakkarena.events.tournamentevents.TimeToStartTournamentEvent;
import no.ntnu.sjakkarena.events.tournamentevents.TournamentStartedEvent;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TournamentEventCreator tournamentEventCreator;

    @Autowired
    private GameCreator gameCreator;

    public void startTournament(int tournamentId) {
        try {
            tournamentRepository.setActive(tournamentId);
            onTournamentStart(tournamentId);
        } catch (TroubleUpdatingDBException e) {
            throw e;
        }
    }

    public Tournament getTournament(int tournamentId) {
        try {
            return tournamentRepository.getTournament(tournamentId);
        } catch (NotInDatabaseException e) {
            throw new NotInDatabaseException(e);
        }
    }

    private void onTournamentStart(int tournamentId) {
        tournamentEventCreator.createAndPublishTournamentStartedEvent(tournamentId);
    }

    private void onTournamentEnd(int tournamentId) {
        tournamentEventCreator.createAndPublishTournamentEndedEvent(tournamentId);
    }

    @EventListener
    public void onTimeToStartTournament(TimeToStartTournamentEvent timeToStartTournamentEvent){
        startTournament(timeToStartTournamentEvent.getTournamentId());
    }

    @EventListener
    public void onTournamentStart(TournamentStartedEvent tournamentStartedEvent) {
        gameCreator.createAndPublishNewGames(tournamentStartedEvent.getTournamentId(), tournamentStartedEvent.getAdaptedMonrad());
    }

    public boolean isTournamentActive(int tournamentId) {
        return tournamentRepository.isActive(tournamentId);
    }

    public void setTournamentPaused(int tournamentId) {
        try {
            tournamentRepository.setInactive(tournamentId);
        } catch (TroubleUpdatingDBException e) {
            throw e;
        }
    }

    public void setTournamentUnpause(int tournamentId) {
        try {
            tournamentRepository.setActive(tournamentId);
        } catch (TroubleUpdatingDBException e) {
            throw e;
        }
    }

    public void endTournament(int tournamentId) {
        try {
            tournamentRepository.endTournament(tournamentId);
            onTournamentEnd(tournamentId);
        } catch (TroubleUpdatingDBException e){
            throw new TroubleUpdatingDBException(e);
        }
    }
}
