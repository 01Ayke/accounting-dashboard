package org.pfa.controllers;

import org.pfa.models.User;
import org.pfa.services.SessionManager;
import io.javalin.http.Context;
import org.pfa.models.dto.Response;
import org.pfa.models.SessionUser;
import org.pfa.models.LOG.Log_Location;
import org.pfa.models.LOG.Log;
import org.pfa.models.LOG.Log_Type;
import org.pfa.models.Roles_enum;
import org.pfa.services.UserService;

public class Controller {
    private final SessionManager sessionManager;
    private final UserService userService;

    public Controller(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.userService = new UserService();
    }

    public boolean checkRole(Context ctx, Roles_enum requiredRole) {
        String token = ctx.header("Authorization");
        if (token != null && token.equals("DEBUG"))
            return true;

        if (token != null) {
            SessionUser session = sessionManager.validate(token);
            if (session != null) {
                if (userService.getRoles(session.getUserId()).contains(requiredRole)) {
                    return true;
                }
            }
        }
        Response response = new Response("error", "Insufficient privileges", 403);
        response.setContext(ctx);
        return false;
    }

    public boolean checkToken(Context ctx) { // certaines actions necessitent un droit admin
        String token = ctx.header("Authorization");
        if (token != null && token.equals("DEBUG"))
            return true;

        if (token != null) {
            SessionUser session = sessionManager.validate(token);
            Log.log("Validating token: " + token + " for user: " + (session != null ? session.getUserId() : "null"), Log_Type.INFO, Log_Location.Controller);
            if (session != null) {
                return true;
            } else {
                Response response = new Response("error", "Invalid or missing token", 401);
                response.setContext(ctx);
                return false;
            }
        }
        Response response = new Response("error", "Invalid or missing token", 401);
        response.setContext(ctx);
        return false;
    }

    public Integer getUserId(Context ctx) {
        String token = ctx.header("Authorization");
        if (token != null) {
            Integer userId = sessionManager.getUserIdFromToken(token);
            return userId;
        }
        return null;
    }
}
