package org.pfa.services;

import org.pfa.models.Dashboard;
import org.pfa.models.File;

public class DashboardService {
    private FileService fileService = new FileService();

    public Dashboard getDashboard(Long id) {
        File file = fileService.getOne(id);
        Dashboard dashboard = new Dashboard(file);
        return dashboard;
    }
}
