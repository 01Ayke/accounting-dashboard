package org.pfa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "utilisateur")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_utilisateur;

    private String mail;
    private String nom;
    private String prenom;
    private String mot_de_passe;

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
}
