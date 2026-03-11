package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.FileController;
import org.pfa.services.SessionManager;

public class FileRoutes implements RouteInterface {
    public static void register(Javalin app, SessionManager sessionManager) {
        FileController fileController = new FileController(sessionManager);
        app.get("/files", FileController::getAll);
        app.get("/files/{id}", FileController::getOne);
        app.post("/file", fileController::create);
    }
}