package org.pfa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "compte")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compte")
    private Integer idCompte;

    @Column(name = "numero_compte")
    private String numeroCompte;
    private String libelle;

    private Float solde = 0.0F;
    private Boolean actif;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dossier")
    @JsonBackReference(value = "file-comptes")
    private File file;

    @OneToMany(mappedBy = "compte", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "compte-ecriture")
    private Set<EcritureComptable> ecrituresComptables;

    @OneToMany(mappedBy = "compteRattachement", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "compte-compte_aux")
    @JsonIgnore
    private Set<CompteAuxiliaire> comptesAuxiliaires;

    public Compte() {}

    public Compte(File file, String numeroCompte, String libelle, Float solde,Boolean actif) {
        this.file = file;
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

    public File getFile() {
        return file;
    }

    public Set<EcritureComptable> getEcrituresComptables() {
        return ecrituresComptables;
    }

    public Set<CompteAuxiliaire> getComptesAuxiliaires() {
        return comptesAuxiliaires;
    }
}
