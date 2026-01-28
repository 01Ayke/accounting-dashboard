package org.pfa;

import io.javalin.Javalin;
import org.pfa.routes.UserRoutes;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);

        UserRoutes.register(app);
        System.out.println("Serveur démarré sur http://localhost:7070");
    }
}
