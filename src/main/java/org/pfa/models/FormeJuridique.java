package org.pfa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "forme_juridique")
public class FormeJuridique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_forme_juridique")
    Integer idFormeJuridique;

    private String libelle;
    private String code;

    public Integer getIdFormeJuridique() {
        return idFormeJuridique;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getCode() {
        return code;
    }
}
