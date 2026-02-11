package org.pfa.controllers;

import io.javalin.http.Context;
import org.pfa.models.Document;
import org.pfa.services.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;

public class DocumentController {

    private static final DocumentService service = new DocumentService();
    
    private static class DocumentInput {
        public String nom;
        public Integer id_type_document;
        public LocalDate dateDocument;
        public Integer id_utilisateur;
    }

    public static void getAll(Context ctx) {
        ctx.json(service.getAllDocuments());
    }

    public static void getOne(Context ctx) {
        Integer id = Integer.parseInt(ctx.pathParam("id"));
        Document doc = service.getDocumentById(id);
        if (doc != null) {
            ctx.json(doc);
        } else {
            ctx.status(404).result("Document non trouvé");
        }
    }

    public static void create(Context ctx) throws Exception {
        try {
            DocumentInput input = ctx.bodyAsClass(DocumentInput.class);

            Document doc = service.createDocument(
                input.nom,
                input.id_type_document,
                LocalDate.parse(input.dateDocument.toString()),
                input.id_utilisateur
            );
            ctx.status(201).json(doc);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(400).result("Erreur: " + e.getMessage());
        }
    }

    public static void update(Context ctx) throws Exception {
        Integer id = Integer.parseInt(ctx.pathParam("id"));
        try {
            DocumentInput input = ctx.bodyAsClass(DocumentInput.class);
            Document updatedDoc = service.updateDocument(
                id,
                input.nom,
                input.id_type_document,
                input.dateDocument
            );
            ctx.json(updatedDoc);
        } catch (Exception e) {
            ctx.status(400).result(e.getMessage());
        }
    }

    public static void delete(Context ctx) {
        Integer id = Integer.parseInt(ctx.pathParam("id"));
        service.deleteDocument(id);
        ctx.status(204);
    }
}