package org.pfa.controllers;

import io.javalin.http.Context;
import java.util.List;

import org.pfa.models.User;
import org.pfa.models.LOG.LOG_Location;
import org.pfa.models.LOG.Log;
import org.pfa.models.LOG.Log_Type;
import org.pfa.models.dto.LoginRequest;
import org.pfa.models.dto.LogoutRequest;
import org.pfa.models.dto.RegisterRequest;
import org.pfa.services.SessionManager;
import org.pfa.services.UserService;
import org.pfa.models.dto.Response;
import org.pfa.models.Roles_enum;

public class UserController extends Controller {
    private static final UserService service = new UserService();
    private final SessionManager sessionManager;

    public UserController(SessionManager sessionManager) {
        super(sessionManager);
        this.sessionManager = sessionManager;
    }

    public void handleLogin(Context ctx) throws Exception {
        LoginRequest login = new LoginRequest(ctx);
        User user = service.getOne(login.getMail(), login.getPassword());
        
        if (user != null) {
            String token = sessionManager.createSession(user.getId());
            Response response = new Response(token, "success", "Login successful", 200);
            Log.log("User " + login.getMail() + " logged in with token: " + token, Log_Type.INFO, LOG_Location.Controller);
            response.setLoginContext(ctx);
        } else {
            Response response = new Response("error", "Invalid email or password", 401);
            Log.log("Failed login attempt for email: " + login.getMail(), Log_Type.ERROR, LOG_Location.Controller);
            response.setContext(ctx);
        }
    }

    public void handleLogout(Context ctx) {
        LogoutRequest logoutRequest = new LogoutRequest(ctx);
        if (super.checkToken(ctx)) {
            String token = logoutRequest.getToken();
            sessionManager.invalidate(token);
            Response response = new Response("success", "Logout successful", 200);
            Log.log("Session invalidated for token: " + token, Log_Type.INFO, LOG_Location.Controller);
            response.setContext(ctx);
            return;
        } 
        Log.log("Invalid token for logout attempt", Log_Type.ERROR, LOG_Location.Controller);
    }

    public void handleRegister(Context ctx) {
        if(super.checkToken(ctx) && super.checkRole(ctx, Roles_enum.ADMIN))
        {
            RegisterRequest registerRequest = new RegisterRequest(ctx);

            if (!registerRequest.isValid()) {
                Response response = new Response("error", "Veuillez remplir tous les champs", 400);
                Log.log("Invalid registration attempt: missing fields", Log_Type.ERROR, LOG_Location.Controller);
                response.setContext(ctx);
                return;
            }

            boolean success = service.register(registerRequest);
            System.out.println("\u001B[32mRegistration attempt for email: " + registerRequest.getMail() + " - Success: " + success + "\u001B[0m");
            if (success) {
                Response response = new Response("success", "Compte créé avec succès", 201); 
                Log.log("User registered successfully with email: " + registerRequest.getMail(), Log_Type.INFO, LOG_Location.Controller);
                response.setContext(ctx);
                return;
            } else {
                Response response = new Response("error", "Cet email est déjà utilisé", 409);
                Log.log("Failed to register user with email: " + registerRequest.getMail(), Log_Type.ERROR, LOG_Location.Controller);
                response.setContext(ctx);
                return;
            }
        }
        Log.log("Permission denied for registration", Log_Type.ERROR, LOG_Location.Controller);
    }

    public void handleGetRole(Context ctx) {
        if(super.checkToken(ctx) && super.checkRole(ctx, Roles_enum.ADMIN)){
            String idStr = ctx.pathParam("id");
            try {
                int userId = Integer.parseInt(idStr);
                List<Roles_enum> roles = service.getRoles(userId);
                List<String> roleNames = roles.stream().map(Enum::name).toList();
                if (roles != null) {
                    Response response = new Response("success", "Rôle récupéré avec succès", 200, roleNames);
                    Log.log("Roles retrieved for user " + userId + ": " + roleNames, Log_Type.INFO, LOG_Location.Controller);
                    response.setContextData(ctx);
                }
            } catch (NumberFormatException e) {
                Response response = new Response("error", "ID utilisateur invalide", 400);
                Log.log("Invalid user ID format: " + idStr, Log_Type.ERROR, LOG_Location.Controller);
                response.setContext(ctx);
            }
        }
        Log.log("Permission denied for retrieving user roles", Log_Type.ERROR, LOG_Location.Controller);
    }
}

