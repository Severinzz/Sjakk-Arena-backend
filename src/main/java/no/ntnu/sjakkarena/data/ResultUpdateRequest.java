package no.ntnu.sjakkarena.data;

import javax.validation.constraints.NotNull;

/**
 * Represents a request to update a result
 */
public class ResultUpdateRequest {


    @NotNull
    int opponent; // The opponent of the requesting player

    @NotNull
    double result; // The new result

    public int getOpponent() {
        return opponent;
    }

    public void setOpponent(int opponent) {
        this.opponent = opponent;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
