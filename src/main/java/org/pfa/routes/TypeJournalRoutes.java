package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.TypeJournalController;
import org.pfa.services.SessionManager;

public class TypeJournalRoutes implements RouteInterface {
    public static void register(Javalin app, SessionManager sessionManager) {
        TypeJournalController typeJournalController = new TypeJournalController(sessionManager);
        app.get("/typeJournal", typeJournalController::getAll);
    }
}
