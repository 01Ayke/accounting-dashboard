package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.JournalController;
import org.pfa.services.SessionManager;

public class JournalRoutes implements RouteInterface {
    public static void register(Javalin app, SessionManager sessionManager) {
        JournalController journalController = new JournalController(sessionManager);
        app.post("/journal", journalController::create);
    }
}
