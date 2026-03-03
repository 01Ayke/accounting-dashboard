package org.pfa.models.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;

import java.util.Date;

public class GrandLivreRequest {
    private static final ObjectMapper mapper = new ObjectMapper();

    private Long idDossier;
    private Date debut;
    private Date fin;

    public Long getIdDossier() {
        return idDossier;
    }

    public Date getDebut() {
        return debut;
    }

    public Date getFin() {
        return fin;
    }
}
