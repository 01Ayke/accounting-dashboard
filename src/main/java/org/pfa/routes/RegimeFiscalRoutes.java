package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.RegimeFiscalController;
import org.pfa.services.SessionManager;

public class RegimeFiscalRoutes implements RouteInterface{
    public static void register(Javalin app, SessionManager sessionManager) {
        RegimeFiscalController regimeFiscalController = new RegimeFiscalController(sessionManager);
        app.get("/regimeFiscal", regimeFiscalController::getAll);
    }
}
