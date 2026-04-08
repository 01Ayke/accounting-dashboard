package org.pfa.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "dossier")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dossier")
    private Integer idDossier;

    private String telephone;
    private String mail;

    @Column(name = "denomination_sociale")
    private String denominationSociale;

    private Float capital;

    @OneToMany(mappedBy = "file", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "file-journal")
    private Set<Journal> journaux;

    @OneToMany(mappedBy = "file", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "file-documents")
    private Set<Document> documents;

    @OneToMany(mappedBy = "file", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "file-comptes")
    private Set<Compte> comptes;

    @ManyToOne
    @JoinColumn(name = "id_forme_juridique")
    private FormeJuridique formeJuridique;

    @ManyToOne
    @JoinColumn(name = "id_regime_fiscal")
    private RegimeFiscal regimeFiscal;

    @ManyToOne
    @JoinColumn(name = "id_regime_tva")
    private RegimeTVA regimeTVA;

    @ManyToOne
    @JoinColumn(name = "id_adresse")
    private Address adresse;

    @ManyToMany
    @JoinTable(
            name = "dossier_representant",
            joinColumns = { @JoinColumn(name = "id_dossier") },
            inverseJoinColumns = { @JoinColumn(name = "id_representant") }
    )
    private Set<Representant> representants;

    @OneToMany(mappedBy = "file")
    private Set<AffectationDossier> utilisateurs;

    public Integer getId() {
        return this.idDossier;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public String getMail() {
        return this.mail;
    }

    public String getDenominationSociale() {
        return this.denominationSociale;
    }

    public Float getCapital() { return capital; }

    public Set<Journal> getJournaux() {
        return journaux;
    }

    public Set<Representant> getRepresentants() {
        return representants;
    }

    public Set<Document> getDocuments() { return documents; }

    public Set<Compte> getComptes() { return comptes; }

    public FormeJuridique getFormeJuridique() { return formeJuridique; }

    public void setFormeJuridique(FormeJuridique formeJuridique) {
        this.formeJuridique = formeJuridique;
    }

    public RegimeFiscal getRegimeFiscal() {
        return regimeFiscal;
    }

    public void setRegimeFiscal(RegimeFiscal regimeFiscal) {
        this.regimeFiscal = regimeFiscal;
    }

    public RegimeTVA getRegimeTVA() {
        return regimeTVA;
    }

    public void setRegimeTVA(RegimeTVA regimeTVA) {
        this.regimeTVA = regimeTVA;
    }

    public Address getAdresse() {
        return adresse;
    }

    public void setAdresse(Address adresse) {
        this.adresse = adresse;
    }

    public Set<AffectationDossier> getUtilisateurs() {
        return utilisateurs;
    }
}
