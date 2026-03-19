package org.pfa.models.dto;

public class AffectationRequest {
    private Integer idUser;
    private Integer idDossier;

    public AffectationRequest() {}

    public Integer getIdUser() { return idUser; }
    public void setIdUser(Integer idUser) { this.idUser = idUser; }
    public Integer getIdDossier() { return idDossier; }
    public void setIdDossier(Integer idDossier) { this.idDossier = idDossier; }
}
