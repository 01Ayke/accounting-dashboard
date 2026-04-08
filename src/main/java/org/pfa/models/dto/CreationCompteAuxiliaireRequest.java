package org.pfa.models.dto;

public class CreationCompteAuxiliaireRequest {
    private Integer idCompteRattachement;
    private Integer idTypeCompteAuxiliaire;
    private String numero;
    private String libelle;

    public Integer getIdCompteRattachement() {
        return idCompteRattachement;
    }

    public String getNumero() {
        return numero;
    }

    public String getLibelle() { return libelle; }

    public Integer getIdTypeCompteAuxiliaire() {
        return idTypeCompteAuxiliaire;
    }
}
