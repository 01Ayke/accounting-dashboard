package org.pfa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "ecriture_comptable")
public class AccountingEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_ecriture_comptable;

    private Date date_saisie;
    private Date date_validation;

    public Date getDate_saisie() { return date_saisie; }

    public Date getDate_validation() {
        return date_validation;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Float debit;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Float credit;

    private String code_operation;

    private Date date_facture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_compte")
    @JsonBackReference
    private Account compte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_journal")
    @JsonBackReference
    private Journal journal;

    public Integer getId_ecriture_comptable() {
        return id_ecriture_comptable;
    }

//    public float getCredit() {
//        return credit;
//    }
//
//    public float getDebit() {
//        return debit;
//    }

    public String getCode_operation() {
        return code_operation;
    }

    public Date getDate_facture() {
        return date_facture;
    }

    public Account getCompte() {
        return compte;
    }

    public Journal getJournal() {
        return journal;
    }
}
