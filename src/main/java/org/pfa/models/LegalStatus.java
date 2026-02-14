package org.pfa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "forme_juridique")
public class LegalStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id_forme_juridique;

    private String libelle;
    private String code;

    public String getLibelle() {
        return libelle;
    }

    public String getCode() {
        return code;
    }
}
