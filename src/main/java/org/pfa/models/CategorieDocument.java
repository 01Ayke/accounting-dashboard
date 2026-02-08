package org.pfa.models;
import jakarta.persistence.*;

@Entity
@Table(name = "categorie_document")
public class CategorieDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_categorie_document;
    
    private String libelle;

    public CategorieDocument() {} 
    public Integer getId() { return id_categorie_document; }
    public String getLibelle() { return libelle; }
}