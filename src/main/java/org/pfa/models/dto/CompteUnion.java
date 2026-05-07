package org.pfa.models.dto;

// type qui peut contenir les informations des comptes et des comptes auxiliaires
public class CompteUnion {
    private Integer idCompte;
    private Integer idCompteAuxiliaire;
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

    public CompteUnion(Integer idCompte, Integer idCompteAuxiliaire, String numeroCompte, String libelle, Float solde, Boolean actif) {
        this.idCompte = idCompte;
        this.idCompteAuxiliaire = idCompteAuxiliaire;
        this.numeroCompte = numeroCompte;
        this.libelle = libelle;
        this.solde = solde;
        this.actif = actif;
    }

    public Integer getIdCompte() {
        return idCompte;
    }

    public Integer getIdCompteAuxiliaire() {
        return idCompteAuxiliaire;
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
