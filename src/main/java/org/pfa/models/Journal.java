package org.pfa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "journal")
public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_journal;

    private String libelle;
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dossier")
    @JsonBackReference(value = "file-journal")
    private File file;

    @OneToMany(mappedBy = "journal", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "ecriture-journal")
    Set<EcritureComptable> ecrituresComptables;

    private String commentaire;

    private Float solde;

    private Boolean actif;

    @ManyToOne
    @JoinColumn(name = "id_type_journal")
    private TypeJournal typeJournal;

    public Journal() {}

    public Journal(String code, String libelle, File file, TypeJournal typeJournal, String commentaire) {
        this.code = code;
        this.libelle = libelle;
        this.file = file;
        this.typeJournal = typeJournal;
        this.commentaire = commentaire;
        this.solde = 0F;
        this.actif = true;
    }

    public File getFile() {
        return file;
    }

    public Integer getIdJournal() {
        return id_journal;
    }

    public String getCode() {
        return code;
    }

    public Set<EcritureComptable> getEcrituresComptables() {
        return ecrituresComptables;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Float getSolde() {
        return solde;
    }

    public Boolean getActif() {
        return actif;
    }
}
