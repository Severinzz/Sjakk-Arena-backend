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
     * Checks if the tournament fields has the correct syntax
     *
     * @param tournament the tournament to be validated
     */
    public static void validateTournament(Tournament tournament) {
        boolean validAdminEmail = validateWithRegEx(tournament.getAdminEmail(), EMAIL_REGEX);

        boolean validTables = validateNonNegativeInteger(tournament.getTables());
        boolean validMaxRounds = validateNonNegativeInteger(tournament.getMaxRounds());

        boolean validStart = validateWithRegEx(tournament.getStart(), TIME_REGEX);
        boolean validEnd = validateWithRegEx(tournament.getEnd(), TIME_REGEX);
        if (validStart && validEnd) {
            boolean endLaterThanStart = LocalTime.parse(tournament.getStart()).isBefore(
                    LocalTime.parse(tournament.getEnd()));
            validEnd = validEnd && endLaterThanStart;
        }
        if (!(validAdminEmail && validStart && validEnd && validTables && validMaxRounds)) {
            throw new ImproperlyFormedDataException();
        }
    }

    /**
     * Returns true if the provided value is null, otherwise falls
     * @return true if the provided value is null, otherwise falls
     */
    private static boolean notNull(Object o){
        return o != null;
    }

    /**
     * Validate a string using regex
     * @param string the string to be validated
     * @param regex the regex to match the string with
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
}
