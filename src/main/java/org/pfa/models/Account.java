package org.pfa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "compte")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_compte;

    private String numero_compte;
    private String libelle;

    private Float solde;
    private Boolean actif;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dossier")
    @JsonBackReference(value = "file-comptes")
    private File file;

    @OneToMany(mappedBy = "compte", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "compte-ecriture")
    private Set<AccountingEntry> ecrituresComptables;

    public Integer getId_compte() {
        return id_compte;
    }

    public String getNumero_compte() {
        return numero_compte;
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

    public Set<AccountingEntry> getEcrituresComptables() {
        return ecrituresComptables;
    }
}
