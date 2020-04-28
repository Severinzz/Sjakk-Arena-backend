package no.ntnu.sjakkarena.services.unauthenticateduser;

import no.ntnu.sjakkarena.EmailSender;
import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.tasks.EndTournamentTask;
import no.ntnu.sjakkarena.tasks.StartTournamentTask;
import no.ntnu.sjakkarena.utils.IDGenerator;
import no.ntnu.sjakkarena.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;

/**
 * This class handles the business logic regarding unauthenticated tournaments
 */
@Service
public class UnauthenticatedTournamentService {

    @Autowired
    private IDGenerator idGenerator;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private JSONCreator jsonCreator;

    /**
     * Signs in as a tournament using an adminUUID
     *
     * @param adminUUID A unique id of the tournament
     * @return A JSON to the signed in tournament
     */
    public String signIn(String adminUUID) {
        try {
            Tournament tournament = tournamentRepository.getTournament(adminUUID);
            return jsonCreator.createResponseToNewTournament(tournament);
        } catch (NotInDatabaseException e) {
            throw e;
        }
    }


    /**
     * Handles the request to add a new tournament
     *
     * @param tournament The tournament to be added
     * @return A JSON response to the new tournament
     */
    public String handleAddTournamentRequest(Tournament tournament) {
        Validator.validateThatStartIsBeforeEnd(tournament);
        addTournamentIDs(tournament);
        tournamentRepository.addNewTournament(tournament);
        scheduleTournamentTasks(tournament);
        //sendEmailToTournamentAdmin(tournament); //Remove comment to send email
        return jsonCreator.createResponseToNewTournament(tournament);
    }

    /**
     * Adds tournament id and adminUUID to the tournament
     *
     * @param tournament the tournament to be given IDs
     */
    private void addTournamentIDs(Tournament tournament) {
        tournament.setId(idGenerator.generateTournamentID());
        tournament.setAdminUUID(idGenerator.generateAdminUUID());
    }

    /**
     * Sends an email to the tournament admin containing the adminUUID
     *
     * @param tournament a tournament
     */
    private void sendEmailToTournamentAdmin(Tournament tournament) {
        EmailSender emailSender = new EmailSender();
        emailSender.sendEmail(tournament.getAdminEmail(), tournament.getTournamentName(),
                "Her er din adminID: " + tournament.getAdminUUID() +
                        ". Bruk denne til å gå til din turneringsside");
    }

    /**
     * Schedules tasks in conjunction with the addition of a tournament
     *
     * @param tournament The recently added tournament
     */
    private void scheduleTournamentTasks(Tournament tournament) {
        scheduleStartTournamentTask(tournament);
        if (tournament.getEnd() != null) {
            scheduleEndTournamentTask(tournament);
        }
    }

    /**
     * Schedules a task to start the specified ournament
     *
     * @param tournament The tournament to be started
     */
    private void scheduleStartTournamentTask(Tournament tournament) {
        try {
            StartTournamentTask startTournamentTask = new StartTournamentTask(applicationEventPublisher);
            startTournamentTask.setTournamentId(tournament.getId());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            TaskScheduler taskScheduler = new ConcurrentTaskScheduler(Executors.newScheduledThreadPool(10));
            taskScheduler.schedule(startTournamentTask, simpleDateFormat.parse(tournament.getStart()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Schedules a task to end the specified tournament
     *
     * @param tournament The tournament to be ended
     */
    private void scheduleEndTournamentTask(Tournament tournament) {
        try {
            EndTournamentTask endTournamentTask = new EndTournamentTask(applicationEventPublisher);
            endTournamentTask.setTournamentId(tournament.getId());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            TaskScheduler taskScheduler = new ConcurrentTaskScheduler(Executors.newScheduledThreadPool(10));
            taskScheduler.schedule(endTournamentTask, simpleDateFormat.parse(tournament.getEnd()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
