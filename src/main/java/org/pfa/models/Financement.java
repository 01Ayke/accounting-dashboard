package org.pfa.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "financement")
public class Financement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_financement")
    private Integer idFinancement;

    @Column(name = "date_financement")
    private Date dateFinancement;

    @Column(name = "duree_mois")
    private Integer dureeMois;

    private Float montant;
    private Float taux;

    @Column(name = "date_premiere_echeance")
    private Date datePremiereEcheance;

    @Column(name = "remboursement_total")
    private Float remboursementTotal;

    @Column(name = "cout_assurance")
    private Float coutAssurance;

    @Column(name = "montant_interet_premiere_echeance")
    private Float montantInteretPremiereEcheance;

    private String commentaire;
    private String intitule;
    private String etablissement;
    private Boolean actif;

    @ManyToOne
    @JoinColumn(name = "id_mode_remboursement")
    private ModeAmortissement modeRemboursement;

    public Integer getIdFinancement() {
        return idFinancement;
    }

    public Date getDateFinancement() {
        return dateFinancement;
    }

    public Integer getDureeMois() {
        return dureeMois;
    }

    public Float getMontant() {
        return montant;
    }

    public Float getTaux() {
        return taux;
    }

    public Date getDatePremiereEcheance() {
        return datePremiereEcheance;
    }

    public Float getRemboursementTotal() {
        return remboursementTotal;
    }

    public Float getCoutAssurance() {
        return coutAssurance;
    }

    public Float getMontantInteretPremiereEcheance() {
        return montantInteretPremiereEcheance;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public String getIntitule() {
        return intitule;
    }

    public String getEtablissement() {
        return etablissement;
    }

    public Boolean getActif() {
        return actif;
    }

    public ModeAmortissement getModeRemboursement() {
        return modeRemboursement;
    }
}
