package org.pfa.models.dto;

public class LigneEcritureDTO {
    private Integer idCompte;
    private Integer idCompteAuxiliaire;
    private Double debit;
    private Double credit;

    public LigneEcritureDTO() {}

    public Integer getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(Integer idCompte) {
        this.idCompte = idCompte;
    }

    public Integer getIdCompteAuxiliaire() {
        return idCompteAuxiliaire;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }
}
