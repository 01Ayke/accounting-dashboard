package org.pfa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "statut_document")
public class DocumentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id_statut_document;

    private String libelle;
    private String code;

    private String description;

    public Integer getId_statut_document() {
        return id_statut_document;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
