package no.ntnu.sjakkarena.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.SubScene;
import nl.martijndwars.webpush.Subscription;

// https://github.com/ralscha/blog2019/blob/master/webpush/src/main/java/ch/rasc/webpush/dto/Subscription.java

public class PushRegistration extends Subscription {
    private String endpoint;
    private final Long expirationTime;

    public final SubscriptionKeys keys;

    public PushRegistration(@JsonProperty("endpoint") String endpoint,
    @JsonProperty("expirationTime") Long expirationTime,
    @JsonProperty("keys") SubscriptionKeys keys){
        this.endpoint = endpoint;
        this.expirationTime = expirationTime;
        this.keys = keys;
    }

    public String getEndpoint(){
        return this.endpoint;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public SubscriptionKeys getKeys(){
        return this.keys;
    }
}
