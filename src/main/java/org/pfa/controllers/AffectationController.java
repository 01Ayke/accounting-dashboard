package org.pfa.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.pfa.models.dto.AffectationRequest;
import org.pfa.models.dto.AffectationResponse;
import org.pfa.models.dto.Response;
import org.pfa.services.AffectationDossierService;
import org.pfa.services.SessionManager;

import java.util.List;

public class AffectationController extends Controller {

    private static final AffectationDossierService affectationService = new AffectationDossierService();
    private final ObjectMapper mapper = new ObjectMapper();

    public AffectationController(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void attribuer(Context ctx) {
        if (super.checkToken(ctx)) {
            try {
                AffectationRequest request = mapper.readValue(ctx.body(), AffectationRequest.class);
                affectationService.attribuer(request.getIdUser(), request.getIdDossier());
                
                Response response = new Response("ok", "Dossier attribué avec succès", 200);
                response.setContext(ctx);
            } catch (JsonProcessingException e) {
                Response response = new Response("error", "Format de la requête invalide", 400);
                response.setContext(ctx);
            } catch (Exception e) {
                Response response = new Response("error", e.getMessage(), 400);
                response.setContext(ctx);
            }
        }
    }

    public void desaffecter(Context ctx) {
        if (super.checkToken(ctx)) {
            try {
                AffectationRequest request = mapper.readValue(ctx.body(), AffectationRequest.class);
                affectationService.desaffecter(request.getIdUser(), request.getIdDossier());
                
                Response response = new Response("ok", "Désaffectation réussie", 200);
                response.setContext(ctx);
            } catch (JsonProcessingException e) {
                Response response = new Response("error", "Format de la requête invalide", 400);
                response.setContext(ctx);
            } catch (Exception e) {
                Response response = new Response("error", e.getMessage(), 400);
                response.setContext(ctx);
            }
        }
    }

    public void lister(Context ctx) {
        if (super.checkToken(ctx)) {
            List<AffectationResponse> historique = affectationService.listerHistorique();
            Response response = new Response("ok", historique, 200);
            response.setContext(ctx);
        }
    }
}
