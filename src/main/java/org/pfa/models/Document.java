package org.pfa.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String nom;
    public String type; // Comptable, Fiscal, ...etc
    
    public Integer clientId;
    
    @Temporal(TemporalType.DATE)
    public Date dateDocument; // date d'écriture du document

    @Temporal(TemporalType.TIMESTAMP)
    public Date dateAjout; // date de l'ajout du document
    
    public Document() {}

    public Document(String nom, String type, Integer clientId, Date dateDocument) {
        this.nom = nom;
        this.type = type;
        this.clientId = clientId;
        this.dateDocument = dateDocument;
        this.dateAjout = new Date();
    }
}