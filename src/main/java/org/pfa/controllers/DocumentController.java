package org.pfa.controllers;

import io.javalin.http.Context;
import org.pfa.models.Document;
import org.pfa.services.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper; // Import pour le JSON

public class DocumentController {

    private static final DocumentService service = new DocumentService();
    private static final ObjectMapper mapper = new ObjectMapper(); 

    public static void getAll(Context ctx) {
        ctx.json(service.getAll());
    }

    public static void getOne(Context ctx) {
        Integer id = Integer.parseInt(ctx.pathParam("id"));
        Document doc = service.getOne(id);
        if (doc != null) {
            ctx.json(doc);
        } else {
            ctx.status(404).result("Document non trouvé");
        }
    }

    public static void create(Context ctx) throws Exception {
        Document doc = mapper.readValue(ctx.body(), Document.class);
        
        if (doc.dateAjout == null) doc.dateAjout = new java.util.Date();

        service.create(doc);
        ctx.status(201).json(doc);
    }

    public static void update(Context ctx) throws Exception {
        Integer id = Integer.parseInt(ctx.pathParam("id"));
        Document existingDoc = service.getOne(id);

        if (existingDoc == null) {
            ctx.status(404).result("Document non trouvé");
            return;
        }

        Document docRecu = mapper.readValue(ctx.body(), Document.class);
        existingDoc.nom = docRecu.nom;
        existingDoc.type = docRecu.type;
        if(docRecu.dateDocument != null) existingDoc.dateDocument = docRecu.dateDocument;

        service.update(existingDoc);
        ctx.json(existingDoc);
    }

    public static void delete(Context ctx) {
        Integer id = Integer.parseInt(ctx.pathParam("id"));
        service.delete(id);
        ctx.status(204);
    }
}