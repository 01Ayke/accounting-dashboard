package org.pfa;

import io.javalin.Javalin;

import org.pfa.routes.DashboardRoutes;
import org.pfa.routes.UserRoutes;
import org.pfa.services.SessionManager;
import org.pfa.routes.DocumentRoutes;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);
        SessionManager sessionManager = new SessionManager();
        DashboardRoutes.register(app);
        UserRoutes.register(app, sessionManager);
        DocumentRoutes.register(app);
        Runtime.getRuntime().addShutdownHook(new Thread(sessionManager::shutdown));
        System.out.println("Serveur démarré sur http://localhost:7070");
    }
}
