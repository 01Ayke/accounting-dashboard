package org.pfa.models;

import jakarta.persistence.*;

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
    
    @Column(name = "is_admin")
    private Boolean isAdmin = false;

    // Constructeur par défaut requis par Hibernate
    public User() {}

    public User(String mail, String mot_de_passe, String nom, String prenom, String code, Boolean isAdmin) {
        this.mail = mail;
        this.mot_de_passe = mot_de_passe;
        this.nom = nom;
        this.prenom = prenom;
        this.code = code;
        this.isAdmin = isAdmin != null ? isAdmin : false;
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

    public Boolean getIsAdmin() {
        return isAdmin;
    }
    public String getCode() {
        return code;
    }
}
