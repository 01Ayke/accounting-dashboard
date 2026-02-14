package org.pfa.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "dossier")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_dossier;

    private String telephone;
    private String mail;
    private String denomination_sociale;
    private Float capital;

    @OneToMany(mappedBy = "file", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Journal> journaux;

    @OneToMany(mappedBy = "file", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Document> documents;

    @ManyToOne
    @JoinColumn(name = "id_forme_juridique")
    private LegalStatus legalStatus;

    @ManyToOne
    @JoinColumn(name = "id_adresse")
    private Address address;

    @ManyToMany
    @JoinTable(
            name = "dossier_representant",
            joinColumns = { @JoinColumn(name = "id_dossier") },
            inverseJoinColumns = { @JoinColumn(name = "id_representant") }
    )
    private Set<Representant> representants;

    public Integer getId() {
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

    public Float getCapital() { return capital; }

    public Set<Journal> getJournaux() {
        return journaux;
    }

    public Set<Representant> getRepresentants() {
        return representants;
    }

    public Set<Document> getDocuments() { return documents; }

    public LegalStatus getLegalStatus() { return legalStatus; }

    public Address getAddress() {
        return address;
    }
}
