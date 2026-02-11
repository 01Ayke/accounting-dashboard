package org.pfa.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "representant")
public class Representant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_representant;

    private String telephone;
    private String mail;
    private String nom;
    private String prenom;
    private String qualite;

    @ManyToMany(mappedBy = "representants")
    private Set<File> files;

    public Integer getId_representant() {
        return id_representant;
    }

    public String getTelephone() {
        return telephone;
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

    public String getQualite() {
        return qualite;
    }
}
