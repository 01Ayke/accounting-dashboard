package org.pfa.models.dto;

import org.pfa.models.Compte;
import org.pfa.models.CompteAuxiliaire;

import java.util.Set;

public class CompteLight {
    private Integer idCompte;
    private String numeroCompte;
    private String libelle;
    private Float solde;
    private Boolean actif;
    private Set<CompteAuxiliaire> comptesAuxiliaires;

    public CompteLight(Compte compte) {
        this.idCompte = compte.getIdCompte();
        this.numeroCompte = compte.getNumeroCompte();
        this.libelle = compte.getLibelle();
        this.solde = compte.getSolde();
        this.actif = compte.getActif();
        this.comptesAuxiliaires = compte.getComptesAuxiliaires();
    }

    public CompteLight(Integer idCompte, String numeroCompte, String libelle, Float solde, Boolean actif, Set<CompteAuxiliaire> comptesAuxiliaires) {
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

    public Set<CompteAuxiliaire> getComptesAuxiliaires() {
        return comptesAuxiliaires;
    }
}
