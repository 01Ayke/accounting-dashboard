package org.pfa.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.pfa.models.GrandLivre;
import org.pfa.models.dto.GrandLivreRequest;
import org.pfa.services.GrandLivreService;

public class GrandLivreController {
    private static final GrandLivreService service = new GrandLivreService();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void getOne(Context ctx) throws JsonProcessingException {
        GrandLivreRequest request = mapper.readValue(ctx.body(), GrandLivreRequest.class);
        GrandLivre grandLivre = service.getOne(request.getIdDossier(), request.getDebut(), request.getFin());
        ctx.json(grandLivre);
    }
}
