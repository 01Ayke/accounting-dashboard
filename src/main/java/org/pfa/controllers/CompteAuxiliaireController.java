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

        File dossier = fileService.getOne(request.getIdDossier());

        if (!request.getNumero().matches("[0-9]*")) {
            Response response = new Response("error", "Numéro de compte invalide", 400);
            response.setContext(ctx);
            return;
        }

        if (dossier == null) {
            Response response = new Response("error", "Dossier invalide", 400);
            response.setContext(ctx);
            return;
        }

        TypeCompteAuxiliaire typeClient = typeCompteAuxiliaireService.fromCode("CLI");
        TypeCompteAuxiliaire typeFournisseur = typeCompteAuxiliaireService.fromCode("FRS");

        Compte compteFournisseur = compteService.findByFileAndNumero(dossier, "401");
        Compte compteClient = compteService.findByFileAndNumero(dossier, "411");

        CompteAuxiliaire compteAuxiliaireClient = new CompteAuxiliaire( request.getLibelle(), "411" + request.getNumero(), compteClient, typeClient);
        compteAuxiliaireService.create(compteAuxiliaireClient);

        CompteAuxiliaire compteAuxiliaireFournisseur = new CompteAuxiliaire( request.getLibelle(), "401" + request.getNumero(), compteFournisseur, typeFournisseur);
        compteAuxiliaireService.create(compteAuxiliaireFournisseur);

        Response response = new Response("ok", "Compte auxiliaire créé", 200);
        response.setContext(ctx);
    }
}
