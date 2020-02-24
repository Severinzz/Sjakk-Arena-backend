package no.ntnu.sjakkarena.utils;

import no.ntnu.sjakkarena.exceptions.ImproperlyFormedDataException;

import no.ntnu.sjakkarena.data.Tournament;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates field values.
 */
public class Validator {

    public static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE); // regex from https://stackoverflow.com/questions/8204680/java-regex-email
    public static final Pattern TIME_REGEX =
            Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]"); // regex from https://mkyong.com/regular-expressions/how-to-validate-time-in-24-hours-format-with-regular-expression/

    /**
     * Checks if the tournament fields are correct
     *
     * @param tournament the tournament to be validated
     */
    public static void tournamentIsValid(Tournament tournament) {
        boolean validAdminEmail = validateWithRegEx(tournament.getAdminEmail(), EMAIL_REGEX);

        boolean validTables = validateNonNegativeInteger(tournament.getTables());
        boolean validMaxRounds = validateNonNegativeInteger(tournament.getMaxRounds());
        boolean validTimes;
        if (tournament.getEnd() == null) {
            validTimes = validateWithRegEx(tournament.getStart(), TIME_REGEX);
        } else {
            validTimes = validateTimes(tournament.getStart(), tournament.getEnd());
        }

        if (!(validAdminEmail && validTimes && validTables && validMaxRounds)) {
            throw new ImproperlyFormedDataException();
        }
    }

    /**
     * Validates the start and end times
     *
     * @param start
     * @param end
     * @return true if start and end times is
     */
    private static boolean validateTimes(String start, String end) {
        boolean validStart = validateWithRegEx(start, TIME_REGEX);
        boolean validEnd = validateWithRegEx(end, TIME_REGEX);
        if (validStart && validEnd) {
            boolean endLaterThanStart = LocalTime.parse(start).isBefore(
                    LocalTime.parse(end));
            return validEnd && endLaterThanStart;
        }
        return false;
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
     * @param result The result to be validated
     * @return true if result is valid
     */
    public static boolean resultIsValid(String result) {
        return (result.equals("1-0") || result.equals("0-1") || result.equals("0.5-0.5"));
    }
}
