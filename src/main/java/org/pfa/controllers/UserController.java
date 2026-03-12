package org.pfa.controllers;

import io.javalin.http.Context;

import org.pfa.models.User;
import org.pfa.models.dto.LoginRequest;
import org.pfa.models.dto.LogoutRequest;
import org.pfa.models.dto.RegisterRequest;
import org.pfa.services.SessionManager;
import org.pfa.services.UserService;
import org.pfa.models.dto.Response;

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
            response.setLoginContext(ctx);
        } else {
            Response response = new Response("error", "Invalid email or password", 401);
            response.setContext(ctx);
        }
    }

    public void handleLogout(Context ctx) {
        LogoutRequest logoutRequest = new LogoutRequest(ctx);
        if (super.checkToken(ctx)) {
            String token = logoutRequest.getToken();
            sessionManager.invalidate(token);
            Response response = new Response("success", "Logout successful", 200);
            response.setContext(ctx);
        } 
    }

    public void handleRegister(Context ctx) {
        if(super.checkToken(ctx) && super.checkRole(ctx, org.pfa.models.Roles_enum.ADMIN))
        {
            RegisterRequest registerRequest = new RegisterRequest(ctx);

            if (!registerRequest.isValid()) {
                Response response = new Response("error", "Veuillez remplir tous les champs", 400);
                response.setContext(ctx);
                return;
            }

            boolean success = service.register(registerRequest);
            System.out.println("\u001B[32mRegistration attempt for email: " + registerRequest.getMail() + " - Success: " + success + "\u001B[0m");
            if (success) {
                Response response = new Response("success", "Compte créé avec succès", 201);
                response.setContext(ctx);
                return;
            } else {
                Response response = new Response("error", "Cet email est déjà utilisé", 409);
                response.setContext(ctx);
            }
        }
    }
}

