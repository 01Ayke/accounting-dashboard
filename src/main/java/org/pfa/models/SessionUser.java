package org.pfa.models;

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
        return System.currentTimeMillis() > expirationTime;
    }

    public void refresh() {
        this.expirationTime = System.currentTimeMillis() + ttl;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}

