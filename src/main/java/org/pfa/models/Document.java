package org.pfa.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;
    @ManyToOne
    @JoinColumn(name = "id_type_document", nullable = false)
    private TypeDocument typeDocument;
    
    @Column(name = "date_document")
    private LocalDate dateDocument; // date d'écriture du document

    @CreationTimestamp
    @Column(name = "date_ajout", updatable = false)
    private LocalDateTime dateAjout; // date de l'ajout du document

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    @JsonIgnoreProperties({"documents", "mot_de_passe"})
    private User user;
    
    public Document() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public LocalDate getDateDocument() { return dateDocument; }
    public void setDateDocument(LocalDate dateDocument) { this.dateDocument = dateDocument; }

    public LocalDateTime getDateAjout() { return dateAjout; }
    public void setDateAjout(LocalDateTime dateAjout) { this.dateAjout = dateAjout; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public void setTypeDocument(TypeDocument typeDocument) { this.typeDocument = typeDocument; }
    public TypeDocument getTypeDocument() { return typeDocument; }

    public Document(String nom, TypeDocument typeDocument, User user, LocalDate dateDocument) {
        this.nom = nom;
        this.typeDocument = typeDocument;
        this.user = user;
        this.dateDocument = dateDocument;
    }
}