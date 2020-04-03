package no.ntnu.sjakkarena.services.tournament;

import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.events.TimeToStartTournamentEvent;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.services.EventService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class TournamentService extends EventService {

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
        createAndPublishTournamentStartedEvent(tournamentId);
    }

    @EventListener
    public void onTimeToStartTournament(TimeToStartTournamentEvent timeToStartTournamentEvent){
        startTournament(timeToStartTournamentEvent.getTournamentId());
    }

    public boolean isTournamentActive(int tournamentId) {
        return tournamentRepository.isActive(tournamentId);
    }
}
