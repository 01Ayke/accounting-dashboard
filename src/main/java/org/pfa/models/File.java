package org.pfa.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "dossier")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_dossier;

    private String telephone;
    private String mail;
    private String denomination_sociale;

    @OneToMany(mappedBy = "file", fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<Journal> journaux;

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

    public Set<Journal> getJournaux() {
        return journaux;
    }
}
