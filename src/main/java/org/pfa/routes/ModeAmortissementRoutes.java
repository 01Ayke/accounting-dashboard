package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.ModeAmortissementController;
import org.pfa.services.SessionManager;

public class ModeAmortissementRoutes implements RouteInterface {
    public static void register(Javalin app, SessionManager sessionManager) {
        ModeAmortissementController modeAmortissementController = new ModeAmortissementController(sessionManager);
        app.get("/modeAmortissement", modeAmortissementController::getAll);
    }
}
