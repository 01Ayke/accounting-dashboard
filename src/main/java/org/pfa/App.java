package org.pfa;

import io.javalin.Javalin;

import org.pfa.routes.*;
import org.pfa.services.SessionManager;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);
        SessionManager sessionManager = new SessionManager();
        DashboardRoutes.register(app, sessionManager);
        UserRoutes.register(app, sessionManager);
        DocumentRoutes.register(app);
        FileRoutes.register(app, sessionManager);
        GrandLivreRoutes.register(app, sessionManager);
        CompteRoutes.register(app, sessionManager);
        Runtime.getRuntime().addShutdownHook(new Thread(sessionManager::shutdown));
        System.out.println("Serveur démarré sur http://localhost:7070") ;
    }
}
