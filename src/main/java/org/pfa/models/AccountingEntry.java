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
    private Long id_ecriture_comptable;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Float debit;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Float credit;

    private String code_operation;

    private Date date_facture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_journal")
    @JsonBackReference
    private Journal journal;

    public Long getId_ecriture_comptable() {
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

    public Journal getJournal() {
        return journal;
    }
}
