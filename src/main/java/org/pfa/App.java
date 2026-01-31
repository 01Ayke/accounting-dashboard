package org.pfa;

import io.javalin.Javalin;
import org.pfa.routes.DashboardRoutes;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);

        DashboardRoutes.register(app);

        System.out.println("Serveur démarré sur http://localhost:7070");
    }
}
