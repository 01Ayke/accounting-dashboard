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

import java.util.List;

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

        Compte compte = new Compte(file, request.getNumeroCompte(), request.getLibelle(), 0.0F,true);
        if (!request.getNumeroCompte().matches("[0-9]*")) {
            Response response = new Response("error", "Numéro de compte invalide", 400);
            response.setContext(ctx);
            return;
        }

        if (compteService.createIfNotExists(compte)) {
            Response response = new Response("ok", "Compte créé", 201);
            response.setContext(ctx);
        }
        else {
            Response response = new Response("error", "Compte déjà existant", 400);
            response.setContext(ctx);
        }
    }

    public void getAll(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            File file = fileService.getOne(id);
            List<Compte> comptes = compteService.findByFile(file);
            Response response = new Response("ok", comptes, 200);
            response.setContext(ctx);
        } catch (Exception e) {
            Response response = new Response("error", "Numéro de dossier invalide", 400);
            response.setContext(ctx);
        }
    }

    public void getAllAgregation(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            File file = fileService.getOne(id);
            List<Compte> comptes = compteService.findByFile(file)
                    .stream()
                    .filter(compte ->
                            compte.getNumeroCompte().toString().matches("(40[0-9])|(41[0-9])"))
                    .toList();
            Response response = new Response("ok", comptes, 200);
            response.setContext(ctx);
        } catch (Exception e) {
            Response response = new Response("error", "Numéro de dossier invalide", 400);
            response.setContext(ctx);
        }
    }
}
