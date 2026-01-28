package org.pfa.controllers;

import io.javalin.http.Context;
import org.pfa.models.User;
import org.pfa.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class UserController {

    private static final UserService service = new UserService();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void getAll(Context ctx) {
        List<User> users = service.getAll();
        ctx.json(users);
    }

    public static void getOne(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        User user = service.getOne(id);
        if (user != null) ctx.json(user);
        else ctx.status(404).result("User not found");
    }

    public static void create(Context ctx) throws Exception {
        User user = mapper.readValue(ctx.body(), User.class);
        service.create(user);
        ctx.status(201).json(user);
    }

    public static void update(Context ctx) throws Exception {
        Long id = Long.parseLong(ctx.pathParam("id"));
        User existing = service.getOne(id);
        if (existing == null) { ctx.status(404).result("User not found"); return; }

        User update = mapper.readValue(ctx.body(), User.class);
        existing.setName(update.getName());
        service.update(existing);
        ctx.json(existing);
    }

    public static void delete(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        service.delete(id);
        ctx.status(204);
    }
}
