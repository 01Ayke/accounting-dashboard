package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.DashboardController;
import org.pfa.services.SessionManager;

public class DashboardRoutes implements RouteInterface {
    public static void register(Javalin app, SessionManager sessionManager) {
        DashboardController dashboardController = new DashboardController(sessionManager);
        app.get("/file/{id}", dashboardController::getOne);
    }
}
