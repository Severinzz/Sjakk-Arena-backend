package no.ntnu.sjakkarena.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents as user.
 */
public class User {

    private int userId;

    /**
     * Constructs a user with the given Id
     *
     * @param userId The id of the user
     */
    public User(int userId) {
        this.userId = userId;
    }

    /**
     * Constructs a user with no data
     */
    public User() {
    }

    @JsonProperty("user_id")
    public int getId() {
        return userId;
    }

    public void setId(int userId) {
        this.userId = userId;
    }

}
