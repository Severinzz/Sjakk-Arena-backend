package no.ntnu.sjakkarena.data;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    private int userId;

    public User() {
    }

    public User(int userId){
        this.userId = userId;
    }

    public int getId() {
        return userId;
    }

    public void setId(int userId) {
        this.userId = userId;
    }

}
