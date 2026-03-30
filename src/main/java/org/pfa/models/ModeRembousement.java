package org.pfa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "mode_remboursement")
public class ModeRembousement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mode_remboursement")
    private Integer idModeRemboursement;

    private String libelle;
    private String code;

    public Integer getIdModeRemboursement() {
        return idModeRemboursement;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getCode() {
        return code;
    }
}
