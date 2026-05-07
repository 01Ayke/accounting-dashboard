package org.pfa.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.pfa.models.Compte;
import org.pfa.models.CompteAuxiliaire;
import org.pfa.models.File;
import org.pfa.models.LOG.Log;
import org.pfa.models.LOG.Log_Location;
import org.pfa.models.LOG.Log_Type;
import org.pfa.models.dto.CompteLight;
import org.pfa.models.dto.CompteUnion;
import org.pfa.models.dto.CreationCompteRequest;
import org.pfa.models.dto.Response;
import org.pfa.services.CompteService;
import org.pfa.services.FileService;
import org.pfa.services.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
            Log.log("Account successfully created", Log_Type.INFO, Log_Location.Controller);
            response.setContext(ctx);
        }
        else {
            Response response = new Response("error", "Compte déjà existant", 400);
            Log.log("Account already exists", Log_Type.ERROR, Log_Location.Controller);
            response.setContext(ctx);
        }
    }

    public void getAll(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            File file = fileService.getOne(id);
            List<Compte> comptes = compteService.findByFile(file);
            Response response = new Response("ok", comptes, 200);
            Log.log("List of accounts successfully retrieved", Log_Type.INFO, Log_Location.Controller);
            response.setContext(ctx);
        } catch (Exception e) {
            Response response = new Response("error", "Numéro de dossier invalide", 400);
            Log.log("Invalid file", Log_Type.ERROR, Log_Location.Controller);
            response.setContext(ctx);
        }
    }

    public void getAllLight(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            File file = fileService.getOne(id);
            List<CompteLight> comptes = compteService.findByFileLight(file);
            List<CompteUnion> allComptes = new ArrayList<>();
            for (CompteLight compteLight: comptes) {
                for (CompteAuxiliaire compteAuxiliaire: compteLight.getComptesAuxiliaires()) {
                    allComptes.add(new CompteUnion(
                            compteAuxiliaire.getIdCompteAuxiliaire(),
                            compteAuxiliaire.getNumeroCompteAuxiliaire(),
                            compteAuxiliaire.getLibelle(),
                            compteAuxiliaire.getSolde(),
                            compteAuxiliaire.getActif()));
                }
                allComptes.add(new CompteUnion(
                        compteLight.getIdCompte(),
                        compteLight.getNumeroCompte(),
                        compteLight.getLibelle(),
                        compteLight.getSolde(),
                        compteLight.getActif()
                ));
            }
            Response response = new Response("ok", allComptes, 200);
            Log.log("List of accounts successfully retrieved", Log_Type.INFO, Log_Location.Controller);
            response.setContext(ctx);
        } catch (Exception e) {
            e.printStackTrace();
            Response response = new Response("error", "Numéro de dossier invalide", 400);
            Log.log("Invalid file", Log_Type.INFO, Log_Location.Controller);
            response.setContext(ctx);
        }
    }

    public void getAllAgregation(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            File file = fileService.getOne(id);
            List<CompteLight> comptes = compteService.findByFileLight(file)
                    .stream()
                    .filter(compte ->
                            compte.getNumeroCompte().toString().matches("(40[0-9])|(41[0-9])"))
                    .toList();
            Response response = new Response("ok", comptes, 200);
            Log.log("Aggregation accounts successfully retrieved", Log_Type.INFO, Log_Location.Controller);
            response.setContext(ctx);
        } catch (Exception e) {
            Response response = new Response("error", "Numéro de dossier invalide", 400);
            Log.log("Invalid file", Log_Type.INFO, Log_Location.Controller);
            response.setContext(ctx);
        }
    }

    public void getAllComptesRattachement(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            File file = fileService.getOne(id);

            System.out.println(file.getDenominationSociale());

            List<Compte> comptes = compteService.findByFile(file)
                    .stream()
                    .filter(compte ->
                            compte.getNumeroCompte().toString().matches("(40[0-9])|(41[0-9])"))
                    .toList();

            List<CompteAuxiliaire> comptesAuxiliaires = new ArrayList<>();

            for (Compte compte: comptes) {
                Set<CompteAuxiliaire> aux = compte.getComptesAuxiliaires();
                for (CompteAuxiliaire compteAux: aux) {
                    if (compteAux.getNumeroCompteAuxiliaire().matches("(40|41)[0-9][0-9][0-9]")) {
                        comptesAuxiliaires.add(compteAux);
                    }
                }
            }

            Response response = new Response("ok", comptesAuxiliaires, 200);
            Log.log("Auxiliary accounts successfully retrieved", Log_Type.INFO, Log_Location.Controller);
            response.setContext(ctx);
        } catch (Exception e) {
            e.printStackTrace();
            Response response = new Response("error", "Numéro de dossier invalide", 400);
            response.setContext(ctx);
        }
    }

    // Retourne les comptes tiers (8 chiffres : 40101001, 41101001…)
    public void getAllComptesTiers(Context ctx) {
        try {
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            File file = fileService.getOne(id);

            List<Compte> comptes = compteService.findByFile(file)
                    .stream()
                    .filter(c -> c.getNumeroCompte().matches("(40[0-9])|(41[0-9])"))
                    .toList();

            List<CompteAuxiliaire> tiers = new ArrayList<>();

            for (Compte compte : comptes) {
                for (CompteAuxiliaire aux : compte.getComptesAuxiliaires()) {
                    // 8 chiffres : 40101001, 41101001…
                    if (aux.getNumeroCompteAuxiliaire().matches("(40|41)[0-9][0-9][0-9][0-9][0-9][0-9]")) {
                        tiers.add(aux);
                    }
                }
            }

            Response response = new Response("ok", tiers, 200);
            response.setContext(ctx);
        } catch (Exception e) {
            e.printStackTrace();
            Response response = new Response("error", "Erreur serveur", 500);
            response.setContext(ctx);
        }
    }
}
