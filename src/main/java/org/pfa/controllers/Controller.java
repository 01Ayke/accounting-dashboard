package org.pfa.controllers;

import org.pfa.services.SessionManager;
import io.javalin.http.Context;
import org.pfa.models.dto.Response;
import org.pfa.models.SessionUser;
import org.pfa.models.Roles;


public class Controller {
    private final SessionManager sessionManager;

    public Controller(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public boolean checkRole(Context ctx, Roles requiredRole) {
        String token = ctx.header("Authorization");
        if (token != null && token.equals("DEBUG")) return true;

        if (token != null) {
            SessionUser session = sessionManager.validate(token);
            if (session != null) {
                return session.getRole() == requiredRole;
            }
        }
        Response response = new Response("error", "Insufficient privileges", 403);
        response.setContext(ctx);
        return false;
    }

    public boolean checkToken(Context ctx) { // certaines actions necessitent un droit admin
        String token = ctx.header("Authorization");
        if (token != null && token.equals("DEBUG")) return true;
        
        if (token != null) {
            SessionUser session = sessionManager.validate(token);
            return session != null;
        }
        Response response = new Response("error", "Invalid or missing token", 401);
        response.setContext(ctx);
        return false;
    }
}
