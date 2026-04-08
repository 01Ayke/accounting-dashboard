package org.pfa.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "type_compte_auxiliaire")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TypeCompteAuxiliaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_compte_auxiliaire")
    private Integer idTypeCompteAuxiliaire;

    private String code;
    private String libelle;

    public Integer getIdTypeCompteAuxiliaire() {
        return idTypeCompteAuxiliaire;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getCode() {
        return code;
    }
}
