package org.pfa.models.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

public class GrandLivreRequest {
    private static final ObjectMapper mapper = new ObjectMapper();

    private Integer idDossier;
    private Date debut;
    private Date fin;

    public Integer getIdDossier() {
        return idDossier;
    }

    public Date getDebut() {
        return debut;
    }

    public Date getFin() {
        return fin;
    }
}
