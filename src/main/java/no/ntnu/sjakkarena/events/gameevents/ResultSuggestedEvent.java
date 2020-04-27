package no.ntnu.sjakkarena.events.gameevents;

import org.springframework.context.ApplicationEvent;

/**
 * An event where a result is suggested. This event is classified as a game event
 */
public class ResultSuggestedEvent extends ApplicationEvent {

    private double result;
    private int opponentId;
    private int gameId;

    /**
     * Constructs a ResultSuggestedEvent
     *
     * @param source     The object on which the Event initially occurred.
     *                   (description from https://docs.oracle.com/javase/8/docs/api/java/util/EventObject.html)
     * @param gameId     The id of the game the result is associated with
     * @param opponentId The opponent of the player suggesting the result
     * @param result     The result being suggested
     */
    public ResultSuggestedEvent(Object source, int gameId, int opponentId, double result) {
        super(source);
        this.result = result;
        this.opponentId = opponentId;
        this.gameId = gameId;
    }

    public double getResult() {
        return result;
    }

    public int getOpponentId() {
        return opponentId;
    }

    public int getGameId() {
        return gameId;
    }
}
