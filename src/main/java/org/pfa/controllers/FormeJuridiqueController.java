package org.pfa.controllers;

import io.javalin.http.Context;
import org.pfa.models.FormeJuridique;
import org.pfa.models.dto.Response;
import org.pfa.services.FormeJuridiqueService;
import org.pfa.services.SessionManager;

public class FormeJuridiqueController extends Controller {
    private static final FormeJuridiqueService service = new FormeJuridiqueService();

    public FormeJuridiqueController(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void getAll(Context ctx) {
        Response response = new Response("ok", service.getAll(), 201);
        response.setContext(ctx);
    }
}
