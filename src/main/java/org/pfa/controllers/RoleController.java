package org.pfa.controllers;
import org.pfa.services.SessionManager;
import io.javalin.http.Context;
import java.util.List;
import java.util.ArrayList;


import org.pfa.models.Roles_enum;
import org.pfa.models.dto.Response;
import org.pfa.services.UserService;

public class RoleController extends Controller {
    private final UserService userService;

    public RoleController(SessionManager sessionManager) {
        super(sessionManager);
        this.userService = new UserService();
    }
    public void get_all_users(Context ctx){
        if(super.checkToken(ctx) && super.checkRole(ctx, Roles_enum.ADMIN)){
            Response response = new Response("success", "Voici la liste de tous les utilisateurs", 200, userService.getAll());
            response.setContextData(ctx);
        }
    }

    public void change_role(Context ctx){
        if(super.checkToken(ctx) && super.checkRole(ctx, Roles_enum.ADMIN)){
            System.out.println("On a passé la verif");
            String idStr = ctx.pathParam("id");
            List<Roles_enum> newRoles = new ArrayList<>();
            for (String roleStr : ctx.formParam("roles").split(",")) {
                newRoles.add(Roles_enum.valueOf(roleStr.trim()));
            }
            boolean success = userService.changeRole(Integer.parseInt(idStr), newRoles);
            if(!success) {
                Response response = new Response("error", "Failed to change role for user " + idStr, 500);
                response.setContext(ctx);
                return;
            }
            Response response = new Response("success", "Rôle de l'utilisateur " + idStr + " changé en " + newRoles, 200);
            response.setContext(ctx);
        }
    }
}
