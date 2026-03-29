package org.pfa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "mode_amortissement")
public class ModeAmortissement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mode_amortissement")
    private Integer idModeAmortissement;

    private String code;

    private String libelle;

    public Integer getIdModeAmortissement() {
        return idModeAmortissement;
    }

    public String getCode() {
        return code;
    }

    public String getLibelle() {
        return libelle;
    }
}
