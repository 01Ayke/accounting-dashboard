package org.pfa.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.pfa.models.*;
import org.pfa.models.dto.Response;
import org.pfa.services.*;

import java.util.List;
import java.util.Map;

public class FileController extends Controller {

    private static final FileService fileService = new FileService();
    private static final AdresseService adresseService = new AdresseService();
    private static final RegimeFiscalService regimeFiscalService = new RegimeFiscalService();
    private static final RegimeTVAService regimeTVAService = new RegimeTVAService();
    private static final FormeJuridiqueService formeJuridiqueService = new FormeJuridiqueService();

    private static final ObjectMapper mapper = new ObjectMapper();
    private final SessionManager sessionManager;

    public FileController(SessionManager sessionManager) {
        super(sessionManager);
        this.sessionManager = sessionManager;
    }

    // Renvoie la liste de tous les dossiers (pour l'écran de sélection des dossiers)
    public static void getAll(Context ctx) {
        List<File> files = fileService.getAll();
        ctx.status(200).json(files);
    }

    public static void getOne(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            File file = fileService.getOne(id);
            if (file != null) {
                ctx.status(200).json(file);
            } else {
                ctx.status(404).json(Map.of("status", "error", "message", "Dossier introuvable"));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of("status", "error", "message", "L'ID doit être un nombre valide"));
        }
    }

    public void create(Context ctx) throws JsonProcessingException {
        if (!super.checkToken(ctx)) {
            return;
        }
        File file = mapper.readValue(ctx.body(), File.class);

        Address fileAdresse = file.getAdresse();
        file.setAdresse(adresseService.createOrGet(fileAdresse));

        RegimeFiscal regimeFiscal = regimeFiscalService.fromCode(file.getRegimeFiscal().getCode());
        if (regimeFiscal != null) {
            file.setRegimeFiscal(regimeFiscal);
        }
        else {
            Response response = new Response("token", "error", "Régime fiscal invalide", 400);
            response.setContext(ctx);
            return;
        }

        RegimeTVA regimeTVA = regimeTVAService.fromCode(file.getRegimeTVA().getCode());
        if (regimeTVA != null) {
            file.setRegimeTVA(regimeTVA);
        }
        else {
            Response response = new Response("token", "error", "Régime TVA invalide", 400);
            response.setContext(ctx);
            return;
        }

        FormeJuridique formeJuridique = formeJuridiqueService.fromCode(file.getFormeJuridique().getCode());
        if (formeJuridique != null) {
            file.setFormeJuridique(formeJuridique);
        }
        else {
            Response response = new Response("token", "error", "Forme juridique invalide    ", 400);
            response.setContext(ctx);
            return;
        }
        System.out.println("après forme juridique");

        fileService.create(file);
        Response response = new Response("token", "ok", "Dossier créé", 200);
        response.setContext(ctx);
    }
}