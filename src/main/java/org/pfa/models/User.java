package org.pfa.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "utilisateur")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_utilisateur;

    @Column(unique = true)
    private String mail;
    private String nom;
    private String prenom;
    private String mot_de_passe;
    private String code;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<AffectationDossier> dossiers;

    @ManyToMany
    @JoinTable(
            name = "utilisateur_role",
            joinColumns = { @JoinColumn(name = "id_utilisateur") },
            inverseJoinColumns = { @JoinColumn(name = "id_role") }
    )
    private Set<Roles> roles;

    // Constructeur par défaut requis par Hibernate
    public User() {}

    public User(String mail, String mot_de_passe, String nom, String prenom, String code, Boolean isAdmin) {
        this.mail = mail;
        this.mot_de_passe = mot_de_passe;
        this.nom = nom;
        this.prenom = prenom;
        if (code.isEmpty()) {
            this.code = prenom + nom;
        }
        else {
            this.code = code;
        }
    }

    public String getMail() {
        return mail;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getMot_de_passe() {
        return mot_de_passe;
    }

    public Integer getId() {
        return id_utilisateur;
    }
    
    public String getCode() {
        return code;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public Set<AffectationDossier> getDossiers() {
        return dossiers;
    }
}
