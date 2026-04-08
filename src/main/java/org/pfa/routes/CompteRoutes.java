package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.CompteController;
import org.pfa.services.SessionManager;

public class CompteRoutes implements RouteInterface {
    public static void register(Javalin app, SessionManager sessionManager) {
        CompteController compteController = new CompteController(sessionManager);
        app.get("/dossier-comptes/{id}", compteController::getAll);
        app.get("/dossier-comptes-agregation/{id}", compteController::getAllAgregation);
        app.get("/dossier-comptes-rattachement/{id}", compteController::getAllComptesRattachement);
    }
}
