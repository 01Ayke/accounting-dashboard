package org.pfa.controllers;
import org.pfa.services.SessionManager;
import io.javalin.http.Context;


import org.pfa.models.Roles;
import org.pfa.models.dto.Response;
import org.pfa.services.UserService;

public class RoleController extends Controller {
    private final UserService userService;
    private final SessionManager sessionManager;

    public RoleController(SessionManager sessionManager) {
        super(sessionManager);
        this.userService = new UserService();
        this.sessionManager = sessionManager;
    }
    public void get_all_users(Context ctx){
        if(super.checkToken(ctx) && super.checkRole(ctx, Roles.ADMIN)){
            Response response = new Response("success", "Voici la liste de tous les utilisateurs", 200, userService.getAll());
            response.setContextData(ctx);
        }
    }

    public void change_role(Context ctx){
        if(super.checkToken(ctx) && super.checkRole(ctx, Roles.ADMIN)){
            String idStr = ctx.pathParam("id");
            Roles newRole = Roles.valueOf(ctx.queryParam("role"));
            boolean success = userService.changeRole(Integer.parseInt(idStr), newRole);
            if(!success) {
                Response response = new Response("error", "Failed to change role for user " + idStr, 500);
                response.setContext(ctx);
                return;
            }
            Response response = new Response("success", "Rôle de l'utilisateur " + idStr + " changé en " + newRole, 200);
            response.setContext(ctx);
        }
    }
}
