package no.ntnu.sjakkarena.services.tournament;

import no.ntnu.sjakkarena.GameCreator;
import no.ntnu.sjakkarena.adaptedmonrad.AtTournamentStartAdaptedMonrad;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.eventcreators.TournamentEventCreator;
import no.ntnu.sjakkarena.events.tournamentevents.TimeToStartTournamentEvent;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
            tournamentRepository.activate(tournamentId);
            tournamentRepository.setStartTime(LocalDateTime.now().toString(), tournamentId);
            onTournamentStart(tournamentId);
        } catch (TroubleUpdatingDBException e) {
            throw new TroubleUpdatingDBException(e);
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
        gameCreator.createAndPublishNewGames(tournamentId, new AtTournamentStartAdaptedMonrad());
    }

    private void onTournamentEnd(int tournamentId) {
        tournamentEventCreator.createAndPublishTournamentEndedEvent(tournamentId);
    }

    @EventListener
    public void onTimeToStartTournament(TimeToStartTournamentEvent timeToStartTournamentEvent){
        startTournament(timeToStartTournamentEvent.getTournamentId());
    }

    public boolean isTournamentActive(int tournamentId) {
        return tournamentRepository.isActive(tournamentId);
    }

    public void setTournamentPaused(int tournamentId) {
        try {
            tournamentRepository.inactivate(tournamentId);
        } catch (TroubleUpdatingDBException e) {
            throw e;
        }
    }

    public void setTournamentUnpause(int tournamentId) {
        try {
            tournamentRepository.activate(tournamentId);
        } catch (TroubleUpdatingDBException e) {
            throw e;
        }
    }

    public void endTournament(int tournamentId) {
        try {
            tournamentRepository.inactivate(tournamentId);
            tournamentRepository.finishTournament(tournamentId);
            tournamentRepository.setEndTime(LocalDateTime.now().toString(), tournamentId);
            onTournamentEnd(tournamentId);
        } catch (TroubleUpdatingDBException e){
            throw new TroubleUpdatingDBException(e);
        }
    }
}
