package org.pfa.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.pfa.models.Address;
import org.pfa.models.File;
import org.pfa.models.dto.Response;
import org.pfa.services.AdresseService;
import org.pfa.services.FileService;
import org.pfa.services.SessionManager;

import java.util.List;
import java.util.Map;

public class FileController extends Controller {
    private static final FileService fileService = new FileService();
    private static final AdresseService adresseService = new AdresseService();
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
        System.out.println("before mapper");
        File file = mapper.readValue(ctx.body(), File.class);
        System.out.println("File correctly mapped");
        System.out.println(file.getAdresse());
        Address fileAdresse = file.getAdresse();
        file.setAdresse(adresseService.createOrGet(fileAdresse));
        fileService.create(file);
        Response response = new Response("token", "ok", "Dossier créé", 200);
        response.setContext(ctx);
    }
}