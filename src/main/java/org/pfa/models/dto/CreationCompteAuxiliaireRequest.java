package org.pfa.models.dto;

public class CreationCompteAuxiliaireRequest {
    private Integer idCompteRattachement;
    private String numero;
    private String libelle;
    private String codeTypeCompteAuxiliaire;

    public Integer getIdCompteRattachement() {
        return idCompteRattachement;
    }

    public String getNumero() {
        return numero;
    }

    public String getLibelle() { return libelle; }

    public String getCodeTypeCompteAuxiliaire() {
        return codeTypeCompteAuxiliaire;
    }
}
