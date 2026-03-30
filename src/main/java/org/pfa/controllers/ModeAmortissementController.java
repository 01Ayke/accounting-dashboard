package org.pfa.controllers;

import io.javalin.http.Context;
import org.pfa.models.dto.Response;
import org.pfa.services.ModeAmortissementService;
import org.pfa.services.SessionManager;

public class ModeAmortissementController extends Controller {
    private ModeAmortissementService service = new ModeAmortissementService();

    public ModeAmortissementController(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void getAll(Context ctx) {
        Response response = new Response("ok", service.getAll(), 201);
        response.setContext(ctx);
    }
}
