package org.pfa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "dossier")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_dossier;

    private String telephone;
    private String mail;
    private String denomination_sociale;

    public Long getId() {
        return this.id_dossier;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public String getMail() {
        return this.mail;
    }

    public String getDenominationSociale() {
        return this.denomination_sociale;
    }

}
