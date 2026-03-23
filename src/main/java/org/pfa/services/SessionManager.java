package org.pfa.services;

import org.pfa.models.SessionUser;
import org.pfa.models.LOG.LOG_Location;
import org.pfa.models.LOG.Log;
import org.pfa.models.LOG.Log_Type;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionManager {

    private static final long TTL = 12 * 60 * 60 * 1000; // 12 hours
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
        Log.log("Creating session for user " + userId, Log_Type.INFO, LOG_Location.Service);
        String token = UUID.randomUUID().toString();
        SessionUser session = new SessionUser(token, userId, TTL);

        sessions.put(token, session);
        userSessions.put(userId, token);
        Log.log("Session created for user " + userId + " with token " + token, Log_Type.INFO, LOG_Location.Service);
        return token;
    }

    public SessionUser validate(String token) {
        SessionUser session = sessions.get(token);

        if (session == null) {
            Log.log("Invalid session token: " + token, Log_Type.ERROR, LOG_Location.Service);
            return null;
        }
        if (session.isExpired()) {
            invalidate(token);
            Log.log("Expired session for user: " + session.getUserId(), Log_Type.ERROR, LOG_Location.Service);
            return null;
        }

        // Sliding session
        session.refresh();
        Log.log("Session validated and refreshed for user: " + session.getUserId(), Log_Type.INFO, LOG_Location.Service);
        return session;
    }

    public void invalidate(String token) {
        SessionUser session = sessions.remove(token);
        if (session != null) {
            userSessions.remove(session.getUserId());
            Log.log("Session invalidated for user " + session.getUserId(), Log_Type.INFO, LOG_Location.Service);
        }
    }

    private void startCleanupTask() {
        scheduler.scheduleAtFixedRate(() -> {
            sessions.entrySet().removeIf(entry -> {
                if (entry.getValue().isExpired()) {
                    userSessions.remove(entry.getValue().getUserId());
                    Log.log("Cleaning up expired session for user " + entry.getValue().getUserId(), Log_Type.INFO, LOG_Location.Service);
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

