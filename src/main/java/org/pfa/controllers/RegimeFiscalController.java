package org.pfa.controllers;

import io.javalin.http.Context;
import org.pfa.models.dto.Response;
import org.pfa.services.RegimeFiscalService;
import org.pfa.services.SessionManager;

public class RegimeFiscalController extends Controller {
    private RegimeFiscalService service = new RegimeFiscalService();

    public RegimeFiscalController(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void getAll(Context ctx) {
        Response response = new Response("ok", service.getAll(), 201);
        response.setContext(ctx);
    }
}
