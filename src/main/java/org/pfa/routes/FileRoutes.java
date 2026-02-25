package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.FileController;

public class FileRoutes implements RouteInterface {
    public static void register(Javalin app) {
        app.get("/files", FileController::getAll);
        app.get("/files/{id}", FileController::getOne);
    }
}