package org.pfa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "ecriture_comptable")
public class EcritureComptable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_ecriture_comptable;

    private Date date_saisie;
    public Date getDate_saisie() { return date_saisie; }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Double debit; // public car sinon problème avec le serializer json

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Double credit;

    private String libelle;

    @Column(name = "id_operateur")
    private Integer idOperateur;

    @Column(name = "code_operation")
    private String codeOperation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_compte")
    @JsonBackReference(value = "compte-ecriture")
    private Compte compte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_journal")
    @JsonBackReference(value = "ecriture-journal")
    private Journal journal;

    public EcritureComptable() {}

    public EcritureComptable(Date date_saisie, Double debit, Double credit, String libelle, Compte compte, Journal journal, Integer idOperateur) {
        this.date_saisie = date_saisie;
        this.debit = debit;
        this.credit = credit;
        this.libelle = libelle;
        this.compte = compte;
        this.journal = journal;
        this.idOperateur = idOperateur;
        this.codeOperation = "SAI";
    }

    public Integer getId_ecriture_comptable() {
        return id_ecriture_comptable;
    }

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    public float getCredit() {
//        return credit;
//    }
//
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    public float getDebit() {
//        return debit;
//    }

    public String getLibelle() { return libelle; }

    public Compte getCompte() {
        return compte;
    }

    public Journal getJournal() {
        return journal;
    }
}
