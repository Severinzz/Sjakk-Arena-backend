package no.ntnu.sjakkarena.utils;

import no.ntnu.sjakkarena.repositories.TournamentRepository;

import java.util.Random;

/**
 * Generates ids
 */
public class IDGenerator {

    private static TournamentRepository tournamentRepository = new TournamentRepository();
    private static int maxNumberOfTournaments = 1000000; // maximum allowed tournaments


    /**
     * Generates a unique tournament ID
     *
     * @return a unique tournament ID
     */
    public static int generateTournamentID() {
        Random random = new Random();
        int tournamentId;
        int i = 0;
        do {
            tournamentId = random.nextInt(maxNumberOfTournaments);
            i++;
        }
        while (tournamentRepository.exists(tournamentId) && i <= maxNumberOfTournaments);
        return tournamentId;
    }

    /**
     * Generates a unique admin UUID
     *
     * @return a unique admin UUID
     */
    public static String generateAdminUUID() {
        int stringLength = 5;
        String adminUUID;
        int i = 0;
        do {
            adminUUID = generateRandomString(stringLength);
            i++;
        }
        while (tournamentRepository.exists(adminUUID) && i <= maxNumberOfTournaments);
        return adminUUID;
    }

    /**
     * Generates a random alphanumerical string
     * Code from https://www.baeldung.com/java-random-string
     *
     * @param length the length of the generated string
     * @return a random alphanumerical string
     */
    private static String generateRandomString(int length) {
        int zero = 48;
        int z = 122;
        Random random = new Random();

        String generatedString = random.ints(zero, z + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}
