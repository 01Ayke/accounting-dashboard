package org.pfa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "adresse")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_adresse;

    private String pays;
    private String ville;
    private String rue;
    private String numero;
    private String complement;

    @Column(name = "code_postal")
    private String codePostal;

    public Integer getId_adresse() {
        return id_adresse;
    }

    public String getPays() {
        return pays;
    }

    public String getVille() {
        return ville;
    }

    public String getRue() {
        return rue;
    }

    public String getNumero() {
        return numero;
    }

    public String getComplement() {
        return complement;
    }

    public String getCodePostal() {
        return codePostal;
    }
}
