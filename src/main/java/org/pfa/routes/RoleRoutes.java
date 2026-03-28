package org.pfa.routes;

import org.pfa.controllers.RoleController;
import org.pfa.services.SessionManager;

import io.javalin.Javalin;

public class RoleRoutes {

    public static void register(Javalin app, SessionManager sessionManager) {
        RoleController roleController = new RoleController(sessionManager);
        app.get("/role-manager", roleController::get_all_users);
        app.post("/role-manager/{id}", roleController::change_role);
        app.post("/is-admin", roleController::is_Admin);
        app.get("/get-all-roles", roleController::get_all_roles);
    }

}
