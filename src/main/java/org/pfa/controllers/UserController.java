package org.pfa.controllers;

import io.javalin.http.Context;

import org.pfa.models.User;
import org.pfa.models.dto.LoginRequest;
import org.pfa.models.dto.LogoutRequest;
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
            response.setContext(ctx);
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
}

