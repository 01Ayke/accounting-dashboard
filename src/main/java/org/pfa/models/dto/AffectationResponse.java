package org.pfa.models.dto;

import java.util.Date;

public class AffectationResponse {
    private Integer idAffectation;
    private Integer idUser;
    private String nomUser;
    private String prenomUser;
    private Integer idDossier;
    private String nomDossier;
    private Date dateAffectation;
    private Date dateFinAffectation;
    private Boolean actif;

    public AffectationResponse() {}

    public Integer getIdAffectation() { return idAffectation; }
    public void setIdAffectation(Integer idAffectation) { this.idAffectation = idAffectation; }
    public Integer getIdUser() { return idUser; }
    public void setIdUser(Integer idUser) { this.idUser = idUser; }
    public String getNomUser() { return nomUser; }
    public void setNomUser(String nomUser) { this.nomUser = nomUser; }
    public String getPrenomUser() { return prenomUser; }
    public void setPrenomUser(String prenomUser) { this.prenomUser = prenomUser; }
    public Integer getIdDossier() { return idDossier; }
    public void setIdDossier(Integer idDossier) { this.idDossier = idDossier; }
    public String getNomDossier() { return nomDossier; }
    public void setNomDossier(String nomDossier) { this.nomDossier = nomDossier; }
    public Date getDateAffectation() { return dateAffectation; }
    public void setDateAffectation(Date dateAffectation) { this.dateAffectation = dateAffectation; }
    public Date getDateFinAffectation() { return dateFinAffectation; }
    public void setDateFinAffectation(Date dateFinAffectation) { this.dateFinAffectation = dateFinAffectation; }
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
}
