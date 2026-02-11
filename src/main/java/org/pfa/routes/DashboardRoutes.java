package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.DashboardController;

public class DashboardRoutes implements RouteInterface {
    public static void register(Javalin app) {
        app.get("/file/{id}", DashboardController::getOne);
    }
}
