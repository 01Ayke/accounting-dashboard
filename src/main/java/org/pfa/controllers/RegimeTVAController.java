package org.pfa.controllers;

import io.javalin.http.Context;
import org.pfa.models.dto.Response;
import org.pfa.services.RegimeTVAService;
import org.pfa.services.SessionManager;

public class RegimeTVAController extends Controller {
    private RegimeTVAService service = new RegimeTVAService();

    public RegimeTVAController(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void getAll(Context ctx) {
        Response response = new Response("ok", service.getAll(), 201);
        response.setContext(ctx);
    }
}
