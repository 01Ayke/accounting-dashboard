package org.pfa.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.pfa.models.dto.CreationEcritureBatchRequest;
import org.pfa.models.dto.Response;
import org.pfa.services.EcritureComptableService;
import org.pfa.services.SessionManager;

public class EcritureComptableController extends Controller {

    private static final EcritureComptableService ecritureService = new EcritureComptableService();
    private final ObjectMapper mapper = new ObjectMapper();

    public EcritureComptableController(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void create(Context ctx) {
        if (super.checkToken(ctx)) {
            try {
                CreationEcritureBatchRequest request = mapper.readValue(ctx.body(), CreationEcritureBatchRequest.class);
                request.setIdOperateur(super.getUserId(ctx));
                ecritureService.createBatch(request);
                
                Response response = new Response("ok", "Ecriture comptable bien saisie", 200);
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
}
