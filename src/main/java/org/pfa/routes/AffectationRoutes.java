package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.AffectationController;
import org.pfa.services.SessionManager;

public class AffectationRoutes {
    public static void register(Javalin app, SessionManager sessionManager) {
        AffectationController controller = new AffectationController(sessionManager);

        app.post("/affectations", controller::attribuer);
        app.post("/affectations/revoke", controller::desaffecter);
        app.get("/affectations", controller::lister);
    }
}
