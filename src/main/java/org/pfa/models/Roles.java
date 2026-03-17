package org.pfa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "role_utilisateur")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_role;
    
    @Column(unique = true)
    private String libelle;
    private String code;
    private String description;
    private Integer niveau_acces;
    private Integer actif;

    public Roles(){

    }

    public Roles_enum getCodeFromeRole(){
        return Roles_enum.valueOf(this.code);
    }

    public Integer getIdRole(){
        return id_role;
    }

}
