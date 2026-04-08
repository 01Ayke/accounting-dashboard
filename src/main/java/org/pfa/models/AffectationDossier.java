package org.pfa.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "comptable_dossier")
public class AffectationDossier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comptable_dossier")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dossier", nullable = false)
    private File file;

    @Column(name = "date_attribution")
    private Date dateAffectation;

    @Column(name = "date_fin_attribution")
    private Date dateFinAffectation;
    private Boolean actif;

    @Column(name = "responsable_principal")
    private Boolean responsablePrincipal;

    public AffectationDossier() {}

    public AffectationDossier(User user, File file, Date dateAffectation, Boolean actif, Boolean responsablePrincipal) {
        this.user = user;
        this.file = file;
        this.dateAffectation = dateAffectation;
        this.actif = actif;
        this.responsablePrincipal = responsablePrincipal;
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
