package org.pfa.routes;

import io.javalin.Javalin;
import org.pfa.controllers.DocumentController;

public class DocumentRoutes {

    public static void register(Javalin app) {
        app.get("/documents", DocumentController::getAll);
        app.post("/documents", DocumentController::create);
        app.get("/documents/{id}", DocumentController::getOne);
        app.put("/documents/{id}", DocumentController::update);
        app.delete("/documents/{id}", DocumentController::delete);
    }
}