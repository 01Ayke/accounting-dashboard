package org.pfa.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.pfa.models.File;
import org.pfa.models.Journal;
import org.pfa.models.TypeJournal;
import org.pfa.models.dto.CreationJournalRequest;
import org.pfa.models.dto.Response;
import org.pfa.services.FileService;
import org.pfa.services.JournalService;
import org.pfa.services.SessionManager;
import org.pfa.services.TypeJournalService;

public class JournalController extends Controller {
    private static final JournalService journalService = new JournalService();
    private static final FileService fileService = new FileService();
    private static final TypeJournalService typeJournalService = new TypeJournalService();

    ObjectMapper mapper = new ObjectMapper();

    public JournalController(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void getByDossier(Context ctx) {
        if (super.checkToken(ctx)) {
            try {
                Integer dossierId = Integer.parseInt(ctx.pathParam("id"));
                var journaux = journalService.getByDossierId(dossierId);
                Response response = new Response("ok", journaux, 200);
                response.setContext(ctx);
            } catch (Exception e) {
                Response response = new Response("error", "Dossier invalide", 400);
                response.setContext(ctx);
            }
        }
    }

    public void create(Context ctx) throws JsonProcessingException {
        if (super.checkToken(ctx)) {
            try {
                CreationJournalRequest request = mapper.readValue(ctx.body(), CreationJournalRequest.class);
                File file = fileService.getOne(request.getIdDossier());
                if (file == null) {
                    Response response = new Response("error", "Numéro de compte invalide", 400);
                    response.setContext(ctx);
                    return;
                }

                TypeJournal typeJournal = typeJournalService.getOne(request.getIdTypeJournal());
                if (typeJournal == null) {
                    Response response = new Response("error", "Type de journal invalide", 400);
                    response.setContext(ctx);
                    return;
                }

                Journal journal = new Journal(request.getCode(), request.getLibelle(), file, typeJournal, request.getCommentaire());
                journalService.create(journal);
                Response response = new Response("ok", "Journal créé", 200);
                response.setContext(ctx);
            } catch (JsonProcessingException e) {
                Response response = new Response("error", "Requête invalide", 400);
                response.setContext(ctx);
            }
        }
    }
}
