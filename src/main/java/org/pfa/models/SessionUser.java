package org.pfa.models;

import org.pfa.models.LOG.Log;
import org.pfa.models.LOG.Log_Type;
import org.pfa.models.LOG.LOG_Location;

public class SessionUser {

    private final String token;
    private final Integer userId;
    private long expirationTime;
    private final long ttl;

    public SessionUser(String token, Integer userId, long ttl) {
        this.token = token;
        this.userId = userId;
        this.ttl = ttl;
        refresh();
    }

    public boolean isExpired() {
        Log.log("Checking if session is expired for user " + userId, Log_Type.INFO, LOG_Location.Service);
        return System.currentTimeMillis() > expirationTime;
    }

    public void refresh() {
        this.expirationTime = System.currentTimeMillis() + ttl;
        Log.log("Session refreshed for user " + userId + ", new expiration time: " + expirationTime, Log_Type.INFO, LOG_Location.Service);
    }

    public Integer getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}

