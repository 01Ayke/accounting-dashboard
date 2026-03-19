package org.pfa.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "affectation_dossier")
public class AffectationDossier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dossier", nullable = false)
    private File file;

    private Date dateAffectation;
    private Date dateFinAffectation;
    private Boolean actif;

    public AffectationDossier() {}

    public AffectationDossier(User user, File file, Date dateAffectation, Boolean actif) {
        this.user = user;
        this.file = file;
        this.dateAffectation = dateAffectation;
        this.actif = actif;
    }

    public Integer getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public File getFile() { return file; }
    public void setFile(File file) { this.file = file; }
    public Date getDateAffectation() { return dateAffectation; }
    public void setDateAffectation(Date dateAffectation) { this.dateAffectation = dateAffectation; }
    public Date getDateFinAffectation() { return dateFinAffectation; }
    public void setDateFinAffectation(Date dateFinAffectation) { this.dateFinAffectation = dateFinAffectation; }
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
}
