package org.pfa.routes;

import io.javalin.Javalin;

import org.pfa.controllers.UserController;
import org.pfa.services.SessionManager;

public class UserRoutes implements RouteInterface {
    public static void register(Javalin app, SessionManager sessionManager) {
        UserController userController = new UserController(sessionManager);
        app.post("/login", userController::handleLogin);
        app.post("/logout", userController::handleLogout);
    }
}
