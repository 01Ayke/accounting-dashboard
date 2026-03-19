package org.pfa.models.dto;

import java.util.Date;
import java.util.List;

public class CreationEcritureBatchRequest {
    private Integer idJournal;
    private Date dateFacture;
    private String libelle;
    private String numeroFacture;
    private String codeOperation;
    private List<LigneEcritureDTO> lignes;

    public CreationEcritureBatchRequest() {}

    public Integer getIdJournal() {
        return idJournal;
    }

    public void setIdJournal(Integer idJournal) {
        this.idJournal = idJournal;
    }

    public Date getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(Date dateFacture) {
        this.dateFacture = dateFacture;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public String getCodeOperation() {
        return codeOperation;
    }

    public void setCodeOperation(String codeOperation) {
        this.codeOperation = codeOperation;
    }

    public List<LigneEcritureDTO> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneEcritureDTO> lignes) {
        this.lignes = lignes;
    }
}
