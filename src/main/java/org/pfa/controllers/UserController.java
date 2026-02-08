package org.pfa.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.pfa.models.LoginRequest;
import org.pfa.models.User;
import org.pfa.services.UserService;

import java.util.Map;

public class UserController {
    private static final UserService service = new UserService();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void handleLogin(Context ctx) throws Exception {
        LoginRequest login = mapper.readValue(ctx.body(), LoginRequest.class);
        User user = service.getOne(login.getMail(), login.getPassword());
        if (user != null)
            ctx.status(201).json(Map.of("status", "success", "message", "Login successful"));
        else
            ctx.status(401).json(Map.of("status", "error", "message", "Invalid credentials"));
    }
}
