package org.pfa;

import io.javalin.Javalin;

import org.pfa.routes.*;
import org.pfa.services.SessionManager;
import org.pfa.models.LOG.Log;
import org.pfa.models.LOG.Log_Type;
import org.pfa.models.LOG.Log_Location;


public class App {
    public static void main(String[] args) {
        Log.clearLogFile();
        Log.openHtmlLogInBrowser();
        Javalin app = Javalin.create().start(7070);
        SessionManager sessionManager = new SessionManager();
        DashboardRoutes.register(app, sessionManager);
        UserRoutes.register(app, sessionManager);
        DocumentRoutes.register(app);
        FileRoutes.register(app, sessionManager);
        GrandLivreRoutes.register(app, sessionManager);
        CompteRoutes.register(app, sessionManager);
        RoleRoutes.register(app, sessionManager);
        FormeJuridiqueRoutes.register(app, sessionManager);
        RegimeFiscalRoutes.register(app, sessionManager);
        RegimeTVARoutes.register(app, sessionManager);
        TypeJournalRoutes.register(app, sessionManager);
        JournalRoutes.register(app, sessionManager);
        EcritureComptableRoutes.register(app, sessionManager);
        AffectationRoutes.register(app, sessionManager);
        Runtime.getRuntime().addShutdownHook(new Thread(sessionManager::shutdown));
        Log.log("Server started on http://localhost:7070", Log_Type.INFO, Log_Location.App);
    }
}
