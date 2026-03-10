package org.pfa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "regime_tva")
public class RegimeTVA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_regime_tva")
    private Integer idRegimeTVA;

    private String libelle;
    private String code;

    public Integer getIdRegimeTVA() {
        return idRegimeTVA;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getCode() {
        return code;
    }
}
