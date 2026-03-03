package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.GrandLivreController;
import org.pfa.services.SessionManager;

public class GrandLivreRoutes {
    public static void register(Javalin app, SessionManager sessionManager) {
        GrandLivreController grandLivreController = new GrandLivreController(sessionManager);
        app.get("/grandlivre", grandLivreController::getOne);
    }

}
