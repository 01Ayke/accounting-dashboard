package org.pfa.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "immobilisation")
public class Immobilisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_immobilisation")
    private Integer idImmobilisation;

    @Column(name = "cout_acquisition")
    private Float coutAcquisition;

    @Column(name = "date_acquisition")
    private Date dateAcquisition;

    @Column(name = "duree_amortissement_economique")
    private Integer dureeAmortissementEconomique;

    @Column(name = "base_amortissement_economique")
    private Float baseAmortissementEconomique;

    @Column(name = "duree_amortissement_fiscal")
    private Integer dureeAmortissementFiscal;

    @Column(name = "base_amortissement_fiscal")
    private Float baseAmortissementFiscal;

    private String commentaire;

    private String intitule;

    private Boolean actif;

    @ManyToOne
    @JoinColumn(name = "id_mode_amortissement_economique")
    private ModeAmortissement modeAmortissementEconomique;

    @ManyToOne
    @JoinColumn(name = "id_mode_amortissement_fiscal")
    private ModeAmortissement modeAmortissementFiscal;

    public Integer getIdImmobilisation() {
        return idImmobilisation;
    }

    public Float getCoutAcquisition() {
        return coutAcquisition;
    }

    public Date getDateAcquisition() {
        return dateAcquisition;
    }

    public Integer getDureeAmortissementEconomique() {
        return dureeAmortissementEconomique;
    }

    public Float getBaseAmortissementEconomique() {
        return baseAmortissementEconomique;
    }

    public Integer getDureeAmortissementFiscal() {
        return dureeAmortissementFiscal;
    }

    public Float getBaseAmortissementFiscal() {
        return baseAmortissementFiscal;
    }

    public String getIntitule() {
        return intitule;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Boolean getActif() {
        return actif;
    }
}
