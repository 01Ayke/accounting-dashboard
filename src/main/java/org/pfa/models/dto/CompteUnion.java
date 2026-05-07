package org.pfa.models.dto;

// type qui peut contenir les informations des comptes et des comptes auxiliaires
public class CompteUnion {
    private Integer idCompte;
    private String numeroCompte;
    private String libelle;
    private Float solde;
    private Boolean actif;

    public CompteUnion(Integer idCompte, String numeroCompte, String libelle, Float solde, Boolean actif) {
        this.idCompte = idCompte;
        this.numeroCompte = numeroCompte;
        this.libelle = libelle;
        this.solde = solde;
        this.actif = actif;
    }

    public Integer getIdCompte() {
        return idCompte;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public String getLibelle() {
        return libelle;
    }

    public Float getSolde() {
        return solde;
    }

    public Boolean getActif() {
        return actif;
    }
}
