package org.pfa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "compte_auxiliaire")
public class CompteAuxiliaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compte_auxiliaire")
    private Integer idCompteAuxilliaire;

    private String libelle;
    private Float solde;
    private Boolean actif;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_compte_rattachement")
    @JsonBackReference(value = "compte-compte_aux")
    private Compte compteRattachement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_compte_auxiliaire")
    private TypeCompteAuxiliaire typeCompteAuxiliaire;

    public CompteAuxiliaire(String libelle, Compte compteRattachement, TypeCompteAuxiliaire typeCompteAuxiliaire) {
        this.libelle = libelle;
        this.compteRattachement = compteRattachement;
        this.typeCompteAuxiliaire = typeCompteAuxiliaire;
        this.actif = true;
    }

    public Integer getIdCompteAuxilliaire() { return idCompteAuxilliaire; }

    public String getLibelle() {
        return libelle;
    }

    public Float getSolde() {
        return solde;
    }

    public Boolean getActif() {
        return actif;
    }

    public TypeCompteAuxiliaire getTypeCompteAuxiliaire() {
        return typeCompteAuxiliaire;
    }

    public Compte getCompte() {
        return compteRattachement;
    }
}
