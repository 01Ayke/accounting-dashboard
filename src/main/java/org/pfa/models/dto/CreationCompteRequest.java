package org.pfa.models.dto;

public class CreationCompteRequest {
    private Integer idDossier;
    private String numeroCompte;
    private String libelle;

    public Integer getIdDossier() {
        return idDossier;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public String getLibelle() {
        return libelle;
    }
}
