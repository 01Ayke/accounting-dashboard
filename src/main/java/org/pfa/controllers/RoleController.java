package org.pfa.controllers;
import org.pfa.services.SessionManager;
import io.javalin.http.Context;
import java.util.List;
import java.util.ArrayList;


import org.pfa.models.Roles_enum;
import org.pfa.models.LOG.Log_Location;
import org.pfa.models.LOG.Log;
import org.pfa.models.LOG.Log_Type;
import org.pfa.models.dto.Response;
import org.pfa.services.UserService;
import org.pfa.services.RoleService;

public class RoleController extends Controller {
    private final UserService userService;
    private final RoleService roleService;
    private final SessionManager sessionManager;

    public RoleController(SessionManager sessionManager) {
        super(sessionManager);
        this.userService = new UserService();
        this.roleService = new RoleService();
        this.sessionManager = sessionManager;
    }
    
    public void get_all_users(Context ctx){
        if(super.checkToken(ctx) && super.checkRole(ctx, Roles_enum.ADMIN)){
            Response response = new Response("success", "Voici la liste de tous les utilisateurs", 200, userService.getAll());
            Log.log("Retrieving all users : " + userService.getAll(), Log_Type.INFO, Log_Location.Controller);
            response.setContextData(ctx);
            return;
        }
        Log.log("Permission denied for retrieving all users", Log_Type.ERROR, Log_Location.Controller);
    }

    public void change_role(Context ctx){
        if(super.checkToken(ctx) && super.checkRole(ctx, Roles_enum.ADMIN)){
            
            String idStr = ctx.pathParam("id");
            Log.log("Changing role for user" + idStr, Log_Type.INFO, Log_Location.Controller);
            List<Roles_enum> newRoles = new ArrayList<>();
            for (String roleStr : ctx.formParam("roles").split(",")) {
                newRoles.add(Roles_enum.valueOf(roleStr.trim()));
            }
            boolean success = userService.changeRole(Integer.parseInt(idStr), newRoles);
            if(!success) {
                Response response = new Response("error", "Failed to change role for user " + idStr, 500);
                Log.log("Failed to change role for user " + idStr, Log_Type.ERROR, Log_Location.Controller);
                response.setContext(ctx);
                return;
            }
            Response response = new Response("success", "Rôle de l'utilisateur " + idStr + " changé en " + newRoles, 200);
            Log.log("Role changed for user " + idStr, Log_Type.INFO, Log_Location.Controller);
            response.setContext(ctx);
            return;
        }
        Log.log("Permission denied for changing role", Log_Type.ERROR, Log_Location.Controller);
    }

    public void is_Admin(Context ctx) {

        if(super.checkToken(ctx)) {
            Integer userId = sessionManager.getUserIdFromToken(ctx.header("Authorization"));
            if(userService.getRoles(userId).contains(Roles_enum.ADMIN)) {
                Response response = new Response("success", "L'utilisateur est un admin", 200);
                Log.log("User is an admin", Log_Type.INFO, Log_Location.Controller);
                response.setContext(ctx);
                return;
            }
            else{
                Response response = new Response("error", "L'utilisateur n'est pas un admin", 500);
                Log.log("User is not an admin", Log_Type.INFO, Log_Location.Controller);
                response.setContext(ctx);
                return;
            }

        }
    }

    public void get_my_roles(Context ctx) {
        if(super.checkToken(ctx)) {
            Integer userId = sessionManager.getUserIdFromToken(ctx.header("Authorization"));
            List<Roles_enum> roles = userService.getRoles(userId);
            List<String> roleNames = roles.stream().map(Enum::name).toList();
            Response response = new Response("success", "Voici vos rôles", 200, roleNames);
            Log.log("Retrieving roles for user " + userId + ": " + roleNames, Log_Type.INFO, Log_Location.Controller);
            response.setContextData(ctx);
            return;
        }
        Log.log("Permission denied for retrieving user roles", Log_Type.ERROR, Log_Location.Controller);
    }

    public void get_all_roles(Context ctx) {
        if(super.checkToken(ctx) && super.checkRole(ctx, Roles_enum.ADMIN)) {
            List<String> roles = roleService.getAllRoles();
            Response response = new Response("success", "Voici la liste de tous les rôles", 200, roles);
            Log.log("Retrieving all roles : " + roles, Log_Type.INFO, Log_Location.Controller);
            response.setContextData(ctx);
            return;
        }
        Log.log("Permission denied for retrieving all roles", Log_Type.ERROR, Log_Location.Controller);
    }
}
