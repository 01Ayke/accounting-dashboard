package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.UserController;

public class UserRoutes implements RouteInterface {
    public static void register(Javalin app) {
        app.post("/login", UserController::handleLogin);
    }
}
