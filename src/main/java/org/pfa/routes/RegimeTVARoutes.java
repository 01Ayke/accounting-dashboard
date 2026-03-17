package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.RegimeTVAController;
import org.pfa.services.SessionManager;

public class RegimeTVARoutes {
    public static void register(Javalin app, SessionManager sessionManager) {
        RegimeTVAController regimeTVAController = new RegimeTVAController(sessionManager);
        app.get("/regimeTVA", regimeTVAController::getAll);
    }
}
