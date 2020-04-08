package no.ntnu.sjakkarena.events.gameevents;

import org.springframework.context.ApplicationEvent;

public class ResultSuggestedEvent extends ApplicationEvent {

    private double result;
    private int opponentId;
    private int gameId;

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
