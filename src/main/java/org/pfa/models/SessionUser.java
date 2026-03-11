package org.pfa.models;

public class SessionUser {

    private final String token;
    private final Integer userId;
    private long expirationTime;
    private final long ttl;
    private final Roles role;

    public SessionUser(String token, Integer userId, long ttl, Roles role) {
        this.token = token;
        this.userId = userId;
        this.role = role;
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

    public Roles getRole() {
        return role;
    }
}

