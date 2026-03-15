package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.FormeJuridiqueController;
import org.pfa.services.SessionManager;

public class FormeJuridiqueRoutes implements RouteInterface{
    public static void register(Javalin app, SessionManager sessionManager) {
        FormeJuridiqueController formeJuridiqueController = new FormeJuridiqueController(sessionManager);
        app.get("/formeJuridique", formeJuridiqueController::getAll);
    }
}
