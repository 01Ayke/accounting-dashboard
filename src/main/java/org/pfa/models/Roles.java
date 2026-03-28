package org.pfa.models;

import org.pfa.models.LOG.LOG_Location;
import org.pfa.models.LOG.Log;
import org.pfa.models.LOG.Log_Type;

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
        Log.log("Getting ID for role " + this.code, Log_Type.INFO, LOG_Location.Model);
        return id_role;
    }

    public String getCode() {
        return code;
    }

}
