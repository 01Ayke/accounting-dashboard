package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.GrandLivreController;

public class GrandLivreRoutes {
    public static void register(Javalin app) {
        app.get("/grandlivre", GrandLivreController::getOne);
    }

}
