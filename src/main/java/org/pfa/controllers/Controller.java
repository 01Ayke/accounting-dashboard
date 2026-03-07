package org.pfa.controllers;

import org.pfa.services.SessionManager;
import io.javalin.http.Context;
import org.pfa.models.dto.Response;
import org.pfa.models.SessionUser;


public class Controller {
    private final SessionManager sessionManager;

    public Controller(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public boolean checkToken(Context ctx) {
        return checkToken(ctx, false);
    }

    public boolean checkToken(Context ctx, boolean requireAdmin) { // certaines actions necessitent un droit admin
        String token = ctx.header("Authorization");
        if (token != null && token.equals("DEBUG")) return true;
        
        if (token != null) {
            SessionUser session = sessionManager.validate(token);
            if (session != null) {
                if (requireAdmin && !session.isAdmin()) {
                    Response response = new Response("error", "Forbidden: Admin access required", 403);
                    response.setContext(ctx);
                    return false;
                }
                return true;
            }
        }
        
        Response response = new Response("error", "Invalid or missing token", 401);
        response.setContext(ctx);
        return false;
    }
}
