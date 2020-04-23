package no.ntnu.sjakkarena.data;

import com.fasterxml.jackson.annotation.JsonProperty;

// https://github.com/ralscha/blog2019/blob/master/webpush/src/main/java/ch/rasc/webpush/dto/SubscriptionKeys.java

public class SubscriptionKeys {
    private final String p256dh;

    private final String auth;

    public SubscriptionKeys(@JsonProperty("p256dh") String p256dh,
                            @JsonProperty("auth") String auth) {
        this.p256dh = p256dh;
        this.auth = auth;
    }

    public String getP256dh() {
        return this.p256dh;
    }

    public String getAuth() {
        return this.auth;
    }
}
