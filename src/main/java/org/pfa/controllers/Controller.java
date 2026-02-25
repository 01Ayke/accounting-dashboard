package org.pfa.controllers;

import org.pfa.services.SessionManager;
import io.javalin.http.Context;
import org.pfa.models.dto.Response;


public class Controller {
    private final SessionManager sessionManager;

    public Controller(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public boolean checkToken(Context ctx) {
        String token = ctx.header("Authorization");
        if(token != null && sessionManager.validate(token) != null) {
            return true;
        } else {
            Response response = new Response("error", "Invalid or missing token", 401);
            response.setContext(ctx);
            return false;
        }
    }
}
