package org.pfa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "regime_fiscal")
public class RegimeFiscal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_regime_fiscal")
    private Integer idRegimeFiscal;

    private String libelle;
    private String code;

    public Integer getIdRegimeFiscal() {
        return idRegimeFiscal;
    }

    public String getCode() {
        return code;
    }

    public String getLibelle() {
        return libelle;
    }
}
