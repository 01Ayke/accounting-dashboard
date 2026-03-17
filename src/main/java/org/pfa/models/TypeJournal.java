package org.pfa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "type_journal")
public class TypeJournal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_journal")
    private Integer idTypeJournal;

    private String code;
    private String libelle;

    public Integer getIdTypeJournal() {
        return idTypeJournal;
    }

    public String getCode() {
        return code;
    }

    public String getLibelle() {
        return libelle;
    }
}
