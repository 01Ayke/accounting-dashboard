package org.pfa.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.pfa.models.Compte;
import org.pfa.models.CompteAuxiliaire;
import org.pfa.models.TypeCompteAuxiliaire;
import org.pfa.models.dto.CreationCompteAuxiliaireRequest;
import org.pfa.models.dto.Response;
import org.pfa.services.CompteAuxiliaireService;
import org.pfa.services.CompteService;
import org.pfa.services.SessionManager;
import org.pfa.services.TypeCompteAuxiliaireService;

public class CompteAuxiliaireController extends Controller {
    private CompteAuxiliaireService compteAuxiliaireService = new CompteAuxiliaireService();
    private TypeCompteAuxiliaireService typeCompteAuxiliaireService = new TypeCompteAuxiliaireService();
    private CompteService compteService = new CompteService();

    private ObjectMapper mapper = new ObjectMapper();

    public CompteAuxiliaireController(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void create(Context ctx) throws JsonProcessingException {
        if (!super.checkToken(ctx)) {
            return;
        }
        CreationCompteAuxiliaireRequest creationCompteAuxiliaireRequest = mapper.readValue(ctx.body(), CreationCompteAuxiliaireRequest.class);

        Compte compte = compteService.find(creationCompteAuxiliaireRequest.getIdCompteRattachement());
        if (compte == null) {
            Response response = new Response("token", "error", "Compte de rattachement invalide", 400);
            response.setContext(ctx);
            return;
        }

        TypeCompteAuxiliaire typeCompteAuxiliaire = typeCompteAuxiliaireService.fromCode(creationCompteAuxiliaireRequest.getCodeTypeCompteAuxiliaire());
        if (typeCompteAuxiliaire == null) {
            Response response = new Response("token", "error", "Type de compte auxiliaire invalide", 400);
            response.setContext(ctx);
            return;
        }

        CompteAuxiliaire compteAuxiliaire = new CompteAuxiliaire(creationCompteAuxiliaireRequest.getLibelle(), compte, typeCompteAuxiliaire);
        compteAuxiliaireService.create(compteAuxiliaire);

        Response response = new Response("token", "ok", "Compte auxiliaire créé", 200);
        response.setContext(ctx);
    }
}
