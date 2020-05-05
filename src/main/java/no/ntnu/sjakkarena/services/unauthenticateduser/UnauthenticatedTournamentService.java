package no.ntnu.sjakkarena.services.unauthenticateduser;

import no.ntnu.sjakkarena.EmailSender;
import no.ntnu.sjakkarena.IDGenerator;
import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.HashedElement;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.tasks.EndTournamentTask;
import no.ntnu.sjakkarena.tasks.StartTournamentTask;
import no.ntnu.sjakkarena.utils.HashGenerator;
import no.ntnu.sjakkarena.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;

import java.util.List;

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
            Tournament tournament = findTournamentByAdminUUID(adminUUID);
            return jsonCreator.createResponseToNewTournament(tournament);
        } catch (NotInDatabaseException e) {
            throw e;
        }
    }

    /**
     * Returns the tournament with the specified adminUUID if such tournament exists
     *
     * @param adminUUID The admin id of the tournament to return
     * @return The tournament with the specified adminUUID if such tournament exists
     */
    private Tournament findTournamentByAdminUUID(String adminUUID) {
        List<Tournament> tournaments = tournamentRepository.getAll();
        boolean found = false;
        int i = 0;
        Tournament foundTournament = null;
        while (!found && i < tournaments.size()){
            Tournament tournament = tournaments.get(i);
            if (tournamentHasAdminUUID(tournament, adminUUID)){
                found = true;
                foundTournament = tournament;
            }
            i++;
        }
        if (foundTournament == null){
            throw new NotInDatabaseException("Couldn't find a tournament with the given adminUUID ");
        }
        return foundTournament;
    }

    /**
     * Returns true if the specified tournament has the specified adminUUID
     *
     * @param tournament The tournament
     * @param adminUUID The adminUUID
     * @return true if the specified tournament has the specified adminUUID
     */
    private boolean tournamentHasAdminUUID(Tournament tournament, String adminUUID){
        try {
            HashedElement hashedElement = HashGenerator.hash(adminUUID, tournament.getSalt());
            String hashedAdminUUID = hashedElement.getHashAsString();
            return hashedAdminUUID.equals(tournament.getHashedAdminUUID());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return false;
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
        hashAdminUUID(tournament);
        tournamentRepository.addNewTournament(tournament);
        scheduleTournamentTasks(tournament);
        //sendEmailToTournamentAdmin(tournament); //Remove comment to send email
        return jsonCreator.createResponseToNewTournament(tournament);
    }

    /**
     * Hash the adminUUID of a tournament
     *
     * @param tournament The tournament that will get its adminUUID hashed.
     */
    private void hashAdminUUID(Tournament tournament) {
        try {
            HashedElement hashedAdminUUID = HashGenerator.hash(tournament.getAdminUUID());
            tournament.setHashedAdminUUID(hashedAdminUUID.getHashAsString());
            tournament.setSalt(hashedAdminUUID.getSaltAsString());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
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
     * Schedules a task to start the specified tournament
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
