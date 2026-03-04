package org.pfa.models;

import java.util.Date;

public class LigneGrandLivre {
    private Date date;
    private String codeJournal;
    private String numeroFacture;
    private String libelle;
    private Double debit;
    private Double credit;
    private Double soldeProgressif;

    public LigneGrandLivre(AccountingEntry ecriture, Double solde) {
        this.date = ecriture.getDate_facture();
        this.codeJournal = ecriture.getJournal().getCode();
        this.numeroFacture = ecriture.getNumero_facture();
        this.libelle = ecriture.getLibelle();
        this.debit = ecriture.debit;
        this.credit = ecriture.credit;
        if (this.debit != null)
            this.soldeProgressif = solde + this.debit;
        else
            this.soldeProgressif = solde - this.credit;
    }

    public Date getDate() {
        return date;
    }

    public String getCodeJournal() {
        return codeJournal;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public String getLibelle() {
        return libelle;
    }

    public Double getDebit() {
        return debit;
    }

    public Double getCredit() {
        return credit;
    }

    public Double getSoldeProgressif() {
        return soldeProgressif;
    }
}
