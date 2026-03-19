package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.EcritureComptableController;
import org.pfa.services.SessionManager;

public class EcritureComptableRoutes {
    public static void register(Javalin app, SessionManager sessionManager) {
        EcritureComptableController controller = new EcritureComptableController(sessionManager);

        app.post("/ecritures", controller::create);
    }
}
