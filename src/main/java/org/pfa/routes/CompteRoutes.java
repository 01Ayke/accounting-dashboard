package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.CompteAuxiliaireController;
import org.pfa.controllers.CompteController;
import org.pfa.services.SessionManager;

public class CompteRoutes implements RouteInterface {
    public static void register(Javalin app, SessionManager sessionManager) {
        CompteAuxiliaireController compteAuxiliaireController = new CompteAuxiliaireController(sessionManager);
        app.post("/compte", compteAuxiliaireController::create);
    }
}
