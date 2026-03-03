package org.pfa.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.eclipse.jetty.server.Request;
import org.pfa.models.GrandLivre;
import org.pfa.models.dto.GrandLivreRequest;
import org.pfa.models.dto.Response;
import org.pfa.services.GrandLivreService;
import org.pfa.services.SessionManager;

public class GrandLivreController extends Controller {
    private static final GrandLivreService service = new GrandLivreService();
    private static final ObjectMapper mapper = new ObjectMapper();
    private final SessionManager sessionManager;

    public GrandLivreController(SessionManager sessionManager) {
        super(sessionManager);
        this.sessionManager = sessionManager;
    }

    public void getOne(Context ctx) throws JsonProcessingException {
        if (!super.checkToken(ctx)) {
            return;
        }
        GrandLivreRequest request = mapper.readValue(ctx.body(), GrandLivreRequest.class);
        GrandLivre grandLivre = service.getOne(request.getIdDossier(), request.getDebut(), request.getFin());
        Response response = new Response("token","success", grandLivre, 200);
        response.setContext(ctx);
    }
}
