package no.ntnu.sjakkarena.utils;

import no.ntnu.sjakkarena.exceptions.ImproperlyFormedDataException;

import no.ntnu.sjakkarena.data.Tournament;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates field values.
 */
public class Validator {

    public static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE); // regex from https://stackoverflow.com/questions/8204680/java-regex-email

    /**
     * Checks if the tournament fields are correct
     *
     * @param tournament the tournament to be validated
     */
    public static void validateTournament(Tournament tournament) {
        boolean validAdminEmail = validateWithRegEx(tournament.getAdminEmail(), EMAIL_REGEX);
        boolean validTables = validateNonNegativeInteger(tournament.getTables());
        boolean validMaxRounds = validateNonNegativeInteger(tournament.getMaxRounds());
        boolean validTimes = true;
        if (tournament.getEnd() != null) {
            validTimes = LocalDateTime.parse(tournament.getStart()).isBefore(
                    LocalDateTime.parse(tournament.getEnd()));
        }
        if (!(validAdminEmail && validTimes && validTables && validMaxRounds)) {
            throw new ImproperlyFormedDataException();
        }
    }

    /**
     * Validate a string using regex
     *
     * @param string the string to be validated
     * @param regex  the regex to match the string with
     * @return true if string match regex, otherwise false
     */
    private static boolean validateWithRegEx(String string, Pattern regex) {
        Matcher matcher = regex.matcher(string);
        return matcher.matches();
    }

    /**
     * Checks if input integer is non negative
     *
     * @param integer the integer to be checked
     * @return true if integer is non negative, otherwise false
     */
    private static boolean validateNonNegativeInteger(int integer) {
        return integer >= 0;
    }

    /**
     * Validates result input
     *
     * @param whitePlayerPoints The result to be validated
     * @return true if result is valid
     */
    public static boolean pointsIsValid(double whitePlayerPoints) {
        return (whitePlayerPoints == 0 || whitePlayerPoints == 0.5 || whitePlayerPoints == 1);
    }
}
