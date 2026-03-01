package org.pfa;

import io.javalin.Javalin;

import org.pfa.routes.*;
import org.pfa.services.SessionManager;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);
        SessionManager sessionManager = new SessionManager();
        DashboardRoutes.register(app);
        UserRoutes.register(app, sessionManager);
        DocumentRoutes.register(app);
        FileRoutes.register(app);
        GrandLivreRoutes.register(app);
        Runtime.getRuntime().addShutdownHook(new Thread(sessionManager::shutdown));
        System.out.println("Serveur démarré sur http://localhost:7070") ;
    }
}
