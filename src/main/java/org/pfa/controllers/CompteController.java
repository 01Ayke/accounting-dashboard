package org.pfa.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.pfa.models.Compte;
import org.pfa.models.File;
import org.pfa.models.dto.CreationCompteRequest;
import org.pfa.models.dto.Response;
import org.pfa.services.CompteService;
import org.pfa.services.FileService;
import org.pfa.services.SessionManager;

public class CompteController extends Controller {
    private static final CompteService compteService = new CompteService();
    private static final FileService fileService = new FileService();

    ObjectMapper mapper = new ObjectMapper();

    public CompteController(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void create(Context ctx) throws JsonProcessingException {
        if (!super.checkToken(ctx)) {
            return;
        }
        CreationCompteRequest request = mapper.readValue(ctx.body(), CreationCompteRequest.class);

        File file = fileService.getOne(request.getIdDossier());

        Compte compte = new Compte(file, request.getNumeroCompte(), request.getLibelle(), true);
        compteService.create(compte);

        Response response = new Response("token", "ok", "Dossier créé", 200);
        response.setContext(ctx);
    }
}
