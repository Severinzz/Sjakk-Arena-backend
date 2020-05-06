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

/**
 * This class handles the business logic regarding tournament information
 */
@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TournamentEventCreator tournamentEventCreator;

    @Autowired
    private GameCreator gameCreator;

    /**
     * Responds to an event where it is time to start a tournament.
     * Starts the tournament.
     *
     * @param timeToStartTournamentEvent An event where it is time to start a tournament
     */
    @EventListener
    public void onTimeToStartTournament(TimeToStartTournamentEvent timeToStartTournamentEvent) {
        startTournament(timeToStartTournamentEvent.getTournamentId());
    }

    /**
     * Starts a tournament
     *
     * @param tournamentId The id of the tournament to be started
     */
    public void startTournament(int tournamentId) {
        try {
            tournamentRepository.activate(tournamentId);
            tournamentRepository.setStartTime(LocalDateTime.now().toString(), tournamentId);
            onTournamentStart(tournamentId);
        } catch (TroubleUpdatingDBException e) {
            throw new TroubleUpdatingDBException(e);
        }
    }

    /**
     * Executes necessary tasks when a tournament has started:
     * - Creates and publishes events
     * - Creates and publishes games
     *
     * @param tournamentId The id of the started tournament
     */
    private void onTournamentStart(int tournamentId) {
        tournamentEventCreator.createAndPublishTournamentStartedEvent(tournamentId);
        gameCreator.createAndPublishNewGames(tournamentId, new AtTournamentStartAdaptedMonrad());
    }

    /**
     * Ends a tournament
     *
     * @param tournamentId The id of the tournament to end
     */
    public void endTournament(int tournamentId) {
        try {
            tournamentRepository.inactivate(tournamentId);
            tournamentRepository.finishTournament(tournamentId);
            tournamentRepository.setEndTime(LocalDateTime.now().toString(), tournamentId);
            onTournamentEnd(tournamentId);
        } catch (TroubleUpdatingDBException e) {
            throw new TroubleUpdatingDBException(e);
        }
    }

    /**
     * Executes necessary tasks when a tournament has ended:
     * - Creates and publishes events
     *
     * @param tournamentId The id of the ended tournament
     */
    private void onTournamentEnd(int tournamentId) {
        tournamentEventCreator.createAndPublishTournamentEndedEvent(tournamentId);
    }

    /**
     * Returns a tournament with the specified id
     *
     * @param tournamentId The id of the tournament to return
     * @return A tournament with the specified id
     */
    public Tournament getTournament(int tournamentId) {
        try {
            return tournamentRepository.getTournament(tournamentId);
        } catch (NotInDatabaseException e) {
            throw new NotInDatabaseException(e);
        }
    }

    /**
     * Returns true if the specified tournament is active
     *
     * @param tournamentId The id of the tournament
     * @return True if the specified tournament is active
     */
    public boolean isTournamentActive(int tournamentId) {
        return tournamentRepository.isActive(tournamentId);
    }

    /**
     * Pauses the specified tournament
     *
     * @param tournamentId The id of the tournament to pause
     */
    public void pauseTournament(int tournamentId) {
        try {
            tournamentRepository.inactivate(tournamentId);
        } catch (TroubleUpdatingDBException e) {
            throw e;
        }
    }

    /**
     * Unpauses the specified tournament
     *
     * @param tournamentId The id of the tournament to unpause
     */
    public void unpauseTournament(int tournamentId) {
        try {
            tournamentRepository.activate(tournamentId);
        } catch (TroubleUpdatingDBException e) {
            throw e;
        }
    }
}
