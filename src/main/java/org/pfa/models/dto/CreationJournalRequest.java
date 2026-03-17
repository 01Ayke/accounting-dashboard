package org.pfa.models.dto;

public class CreationJournalRequest {
    private Integer idDossier;
    private Integer idTypeJournal;
    private String code;
    private String libelle;
    private String commentaire;

    public Integer getIdDossier() {
        return idDossier;
    }

    public Integer getIdTypeJournal() {
        return idTypeJournal;
    }

    public String getCode() {
        return code;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getCommentaire() {
        return commentaire;
    }
}
