package org.pfa.controllers;

import io.javalin.http.Context;
import org.pfa.models.File;
import org.pfa.services.FileService;

import java.util.List;
import java.util.Map;

public class FileController {
    private static final FileService service = new FileService();

    // Renvoie la liste de tous les dossiers (pour l'écran de sélection des dossiers)
    public static void getAll(Context ctx) {
        List<File> files = service.getAll();
        ctx.status(200).json(files);
    }

    public static void getOne(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            File file = service.getOne(id);
            if (file != null) {
                ctx.status(200).json(file);
            } else {
                ctx.status(404).json(Map.of("status", "error", "message", "Dossier introuvable"));
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json(Map.of("status", "error", "message", "L'ID doit être un nombre valide"));
        }
    }
}