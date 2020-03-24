package no.ntnu.sjakkarena.data;

import javax.validation.constraints.NotNull;

public class ResultUpdateRequest {


    @NotNull
    int opponent;


    @NotNull
    double result;

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
