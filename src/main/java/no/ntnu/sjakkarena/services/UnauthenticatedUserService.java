package no.ntnu.sjakkarena.services;

import no.ntnu.sjakkarena.EmailSender;
import no.ntnu.sjakkarena.JSONCreator;
import no.ntnu.sjakkarena.data.Player;
import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.events.NewPlayerAddedEvent;
import no.ntnu.sjakkarena.exceptions.NameAlreadyExistsException;
import no.ntnu.sjakkarena.exceptions.TroubleUpdatingDBException;
import no.ntnu.sjakkarena.exceptions.NotInDatabaseException;
import no.ntnu.sjakkarena.repositories.PlayerRepository;
import no.ntnu.sjakkarena.repositories.TournamentRepository;
import no.ntnu.sjakkarena.tasks.StartTournamentTask;
import no.ntnu.sjakkarena.utils.IDGenerator;
import no.ntnu.sjakkarena.utils.PlayerIcons;
import no.ntnu.sjakkarena.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Executors;

@Service
public class UnauthenticatedUserService {

    private TaskScheduler taskScheduler;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private JSONCreator jsonCreator = new JSONCreator();

    public String handleAddPlayerRequest(Player player) {
        if (playerRepository.doesPlayerExist(player)){
            throw new NameAlreadyExistsException("Name already take, try a new one!");
        }
        try {
            player.setIcon(PlayerIcons.getRandomFontAwesomeIcon());
            int userId = playerRepository.addNewPlayer(player);
            onNewPlayerAdd(player.getTournamentId());
            return jsonCreator.createResponseToNewPlayer(userId);
        } catch (TroubleUpdatingDBException e) {
            throw e;
        }
    }

    public String handleAddTournamentRequest(Tournament tournament) {
        Validator.validateThatStartIsBeforeEnd(tournament);
        addTournamentIDs(tournament);
        tournamentRepository.addNewTournament(tournament);
        setUpTaskSchedulerAndScheduleStartTournamentTask(tournament);
        //sendEmailToTournamentAdmin(tournament); //Remove comment to send email
        return jsonCreator.createResponseToNewTournament(tournament);
    }

    private void setUpTaskSchedulerAndScheduleStartTournamentTask(Tournament tournament) {
        taskScheduler = new ConcurrentTaskScheduler(Executors.newScheduledThreadPool(10));
        scheduleStartTournamentTask(tournament);
    }

    private void scheduleStartTournamentTask(Tournament tournament) {
        try {
            StartTournamentTask startTournamentTask = new StartTournamentTask(applicationEventPublisher);
            startTournamentTask.setTournamentId(tournament.getId());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

            taskScheduler.schedule(startTournamentTask, simpleDateFormat.parse(tournament.getStart()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String signIn(String adminUUID){
        try {
            Tournament tournament = tournamentRepository.getTournament(adminUUID);
            return jsonCreator.createResponseToNewTournament(tournament);
        } catch(NotInDatabaseException e){
            throw e;
        }
    }

    private void onNewPlayerAdd(int tournamentId) {
        NewPlayerAddedEvent newPlayerAddedEvent = createNewPlayerAddedEvent(tournamentId);
        applicationEventPublisher.publishEvent(newPlayerAddedEvent);
    }

    private NewPlayerAddedEvent createNewPlayerAddedEvent(int tournamentId){
        boolean tournamentHasStarted = tournamentRepository.getTournament(tournamentId).isActive();
        List<Player> players = playerRepository.getPlayersInTournament(tournamentId);
        List<Player> leaderBoard = playerRepository.getLeaderBoard(tournamentId);
        return new NewPlayerAddedEvent(this, players, leaderBoard, tournamentId, tournamentHasStarted);
    }

    /**
     * Adds tournament id and adminUUID to the tournament
     *
     * @param tournament the tournament to be given IDs
     */
    private void addTournamentIDs(Tournament tournament) {
        tournament.setId(IDGenerator.generateTournamentID());
        tournament.setAdminUUID(IDGenerator.generateAdminUUID());
    }

    /**
     * Sends an email to the tournament admin containing the adminUUID
     *
     * @param tournament a tournament
     */
    private void sendEmailToTournamentAdmin(Tournament tournament) {
        EmailSender emailSender = new EmailSender();
        emailSender.sendEmail(tournament.getAdminEmail(), tournament.getTournamentName(),
                "Her er din adminUUID: " + tournament.getAdminUUID() +
                        ". Bruk denne til å gå til din turneringsside");
    }
}
