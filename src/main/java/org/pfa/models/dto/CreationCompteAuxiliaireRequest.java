package org.pfa.models.dto;

public class CreationCompteAuxiliaireRequest {
    private Integer idDossier;
    private String numero;
    private String libelle;

    public Integer getIdDossier() {
        return idDossier;
    }

    public String getNumero() {
        return numero;
    }

    public String getLibelle() { return libelle; }

}
