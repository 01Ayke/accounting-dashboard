package org.pfa.services;

import org.pfa.models.SessionUser;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionManager {

    private static final long TTL = 30 * 60 * 1000; // 30 min
    private static final long CLEAN_INTERVAL = 5 * 60 * 1000;

    private final Map<String, SessionUser> sessions = new ConcurrentHashMap<>();
    private final Map<Integer, String> userSessions = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    public SessionManager() {
        startCleanupTask();
    }

    public String createSession(Integer userId) {

        // Supprime ancienne session si existe
        if (userSessions.containsKey(userId)) {
            String oldToken = userSessions.get(userId);
            sessions.remove(oldToken);
        }
        
        System.out.println("Creating session for user " + userId);

        String token = UUID.randomUUID().toString();
        SessionUser session = new SessionUser(token, userId, TTL);

        sessions.put(token, session);
        userSessions.put(userId, token);

        return token;
    }

    public SessionUser validate(String token) {
        SessionUser session = sessions.get(token);

        if (session == null) return null;

        if (session.isExpired()) {
            invalidate(token);
            return null;
        }

        // Sliding session
        session.refresh();
        return session;
    }

    public void invalidate(String token) {
        SessionUser session = sessions.remove(token);
        if (session != null) {
            userSessions.remove(session.getUserId());
        }
    }

    private void startCleanupTask() {
        scheduler.scheduleAtFixedRate(() -> {
            sessions.entrySet().removeIf(entry -> {
                if (entry.getValue().isExpired()) {
                    userSessions.remove(entry.getValue().getUserId());
                    return true;
                }
                return false;
            });
        }, CLEAN_INTERVAL, CLEAN_INTERVAL, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}

