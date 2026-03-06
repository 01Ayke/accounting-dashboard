package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.CompteController;
import org.pfa.services.SessionManager;

public class CompteRoutes implements RouteInterface {
    public static void register(Javalin app, SessionManager sessionManager) {
        CompteController compteController = new CompteController(sessionManager);
        app.post("/compte", compteController::create);
    }
}
