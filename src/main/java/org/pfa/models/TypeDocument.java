package org.pfa.models;
import jakarta.persistence.*;

@Entity
@Table(name = "type_document")
public class TypeDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_type_document;
    
    private String libelle;

    @ManyToOne // Un type appartient à une seule catégorie
    @JoinColumn(name = "id_categorie_document")
    private CategorieDocument categorie;

    public TypeDocument() {}
    public Integer getId() { return id_type_document; }
    public String getLibelle() { return libelle; }
}