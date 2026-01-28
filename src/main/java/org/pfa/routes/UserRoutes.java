package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.UserController;

public class UserRoutes {

    public static void register(Javalin app) {
        app.get("/users", UserController::getAll);
        app.post("/users", UserController::create);
        app.get("/users/{id}", UserController::getOne);
        app.put("/users/{id}", UserController::update);
        app.delete("/users/{id}", UserController::delete);
    }
}
