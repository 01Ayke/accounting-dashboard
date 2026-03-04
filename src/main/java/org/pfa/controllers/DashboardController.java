package org.pfa.controllers;

import io.javalin.http.Context;
import org.pfa.models.Dashboard;
import org.pfa.models.dto.Response;
import org.pfa.services.DashboardService;
import org.pfa.services.SessionManager;

public class DashboardController extends Controller {
    private static final DashboardService dashboardService = new DashboardService();
    private final SessionManager sessionManager;

    public DashboardController(SessionManager sessionManager) {
        super(sessionManager);
        this.sessionManager = sessionManager;
    }

    public void getOne(Context ctx) {
        if (!super.checkToken(ctx)) {
            return;
        }
        Long id = Long.parseLong(ctx.pathParam("id"));
        Dashboard dashboard = dashboardService.getOne(id);
        if (dashboard.getFile() != null) {
            Response response = new Response("token", "success", dashboard, 200);
            response.setContext(ctx);
        }
        else {
            Response response = new Response("token", "error", "dossier non trouvé", 401);
            response.setContext(ctx);
        }
    }
}
