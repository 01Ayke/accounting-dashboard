package org.pfa.controllers;

import io.javalin.http.Context;
import org.pfa.models.Dashboard;
import org.pfa.services.DashboardService;
import org.pfa.services.FileService;

public class DashboardController {
    private static final DashboardService dashboardService = new DashboardService();

    public static void getOne(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Dashboard dashboard = dashboardService.getDashboard(id);
        System.out.println(dashboard);
        if (dashboard.getFile() != null)
            ctx.json(dashboard);
        else
            ctx.status(404).result("Dashboard not found");
    }
}
