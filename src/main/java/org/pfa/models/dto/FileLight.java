package org.pfa.models.dto;

import org.pfa.models.File;
import org.pfa.models.Representant;

import java.util.Set;

// dossier ne contenant que les informations essentielles pour un chargement plus rapide pour la page d'accueil
public class FileLight {
    private Integer idDossier;
    private String denominationSociale;
    private String telephone;
    private String mail;
    private Set<Representant> representants;

    public FileLight(File file) {
        this.denominationSociale = file.getDenominationSociale();
        this.mail = file.getMail();
        this.telephone = file.getTelephone();
        this.idDossier = file.getId();
        this.representants = file.getRepresentants();
    }

    public String getDenominationSociale() {
        return denominationSociale;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getMail() {
        return mail;
    }

    public Integer getId() {
        return idDossier;
    }

    public Set<Representant> getRepresentants() {
        return representants;
    }
}
