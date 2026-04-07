package org.pfa.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.pfa.models.Compte;
import org.pfa.models.CompteAuxiliaire;
import org.pfa.models.File;
import org.pfa.models.TypeCompteAuxiliaire;
import org.pfa.models.dto.CreationCompteAuxiliaireRequest;
import org.pfa.models.dto.Response;
import org.pfa.services.*;

public class CompteAuxiliaireController extends Controller {
    private CompteAuxiliaireService compteAuxiliaireService = new CompteAuxiliaireService();
    private TypeCompteAuxiliaireService typeCompteAuxiliaireService = new TypeCompteAuxiliaireService();
    private CompteService compteService = new CompteService();
    private FileService fileService = new FileService();

    private ObjectMapper mapper = new ObjectMapper();

    public CompteAuxiliaireController(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void create(Context ctx) throws JsonProcessingException {
        if (!super.checkToken(ctx)) {
            return;
        }
        CreationCompteAuxiliaireRequest request = mapper.readValue(ctx.body(), CreationCompteAuxiliaireRequest.class);

        String nouveauNumero = request.getNumero();

        Compte compteRattachement = compteService.find(request.getIdCompteRattachement());

        if (!request.getNumero().matches("[0-9]*")|| !nouveauNumero.contains(compteRattachement.getNumeroCompte())) {
            Response response = new Response("error", "Numéro de compte invalide", 400);
            response.setContext(ctx);
            return;
        }

        TypeCompteAuxiliaire type = typeCompteAuxiliaireService.getOne(request.getIdTypeCompteAuxiliaire());

        try {
            CompteAuxiliaire compteAuxiliaireClient = new CompteAuxiliaire(request.getLibelle(), request.getNumero(), compteRattachement, type);
            compteAuxiliaireService.create(compteAuxiliaireClient);
        } catch (Exception e) {
            Response response = new Response("error", "Compte déjà existant", 400);
            response.setContext(ctx);
            return;
        }

        Response response = new Response("ok", "Compte auxiliaire créé", 201);
        response.setContext(ctx);
    }
}
