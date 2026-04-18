package org.pfa.models.dto;

import java.util.Date;
import java.util.List;

public class CreationEcritureBatchRequest {
    private Integer idJournal;
    private Integer idOperateur;
    private Date dateSaisie;
    private String libelle;
    private List<LigneEcritureDTO> lignes;

    public CreationEcritureBatchRequest() {}

    public Integer getIdJournal() {
        return idJournal;
    }

    public void setIdJournal(Integer idJournal) {
        this.idJournal = idJournal;
    }

    public Integer getIdOperateur() {
        return idOperateur;
    }

    public void setIdOperateur(Integer idOperateur) {
        this.idOperateur = idOperateur;
    }

    public Date getDateSaisie() {
        return dateSaisie;
    }

    public void setDateSaisie(Date dateSaisie) {
        this.dateSaisie = dateSaisie;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<LigneEcritureDTO> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneEcritureDTO> lignes) {
        this.lignes = lignes;
    }
}
