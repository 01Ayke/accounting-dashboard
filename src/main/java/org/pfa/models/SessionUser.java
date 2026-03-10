package org.pfa.models;

public class SessionUser {

    private final String token;
    private final Integer userId;
    private final boolean isAdmin;
    private long expirationTime;
    private final long ttl;

    public SessionUser(String token, Integer userId, boolean isAdmin, long ttl) {
        this.token = token;
        this.userId = userId;
        this.isAdmin = isAdmin;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getToken() {
        return token;
    }
}

