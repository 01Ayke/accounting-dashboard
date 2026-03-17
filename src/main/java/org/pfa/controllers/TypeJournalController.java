package org.pfa.controllers;

import io.javalin.http.Context;
import org.pfa.models.dto.Response;
import org.pfa.services.SessionManager;
import org.pfa.services.TypeJournalService;

public class TypeJournalController extends Controller {
    private TypeJournalService typeJournalService = new TypeJournalService();

    public TypeJournalController(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void getAll(Context ctx) {
        Response response = new Response("ok", typeJournalService.getAll(), 201);
        response.setContext(ctx);
    }
}
