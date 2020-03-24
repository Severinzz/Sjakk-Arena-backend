package no.ntnu.sjakkarena.utils;

import no.ntnu.sjakkarena.data.Tournament;
import no.ntnu.sjakkarena.exceptions.ImproperlyFormedDataException;

import java.time.LocalDateTime;

/**
 * Validates field values.
 */
public class Validator {

    /**
     * Checks if the tournament fields are correct
     *
     * @param tournament the tournament to be validated
     */
    public static void validateThatStartIsBeforeEnd(Tournament tournament) {
        boolean validTimes = true;
        if (tournament.getEnd() != null) {
            validTimes = LocalDateTime.parse(tournament.getStart()).isBefore(
                    LocalDateTime.parse(tournament.getEnd()));
        }
        if (!validTimes) {
            throw new ImproperlyFormedDataException();
        }
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
