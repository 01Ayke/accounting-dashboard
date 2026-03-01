package org.pfa.models.dto;

import java.util.Date;

public class GrandLivreRequest {
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
