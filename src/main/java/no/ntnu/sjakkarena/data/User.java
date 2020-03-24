package no.ntnu.sjakkarena.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private int userId;

    public User() {
    }

    public User(int userId){
        this.userId = userId;
    }
    @JsonProperty("user_id")
    public int getId() {
        return userId;
    }

    public void setId(int userId) {
        this.userId = userId;
    }

}
