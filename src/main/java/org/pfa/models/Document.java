package org.pfa.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_document;

    private String nom_fichier;
    @ManyToOne
    @JoinColumn(name = "id_type_document", nullable = false)
    private TypeDocument typeDocument;
    
    @Column(name = "date_document")
    private LocalDate dateDocument; // date d'écriture du document

    @CreationTimestamp
    @Column(name = "date_ajout", updatable = false)
    private LocalDateTime dateAjout; // date de l'ajout du document

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    @JsonIgnoreProperties({"documents", "mot_de_passe"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dossier")
    @JsonBackReference(value = "file-documents")
    private File file;

    @ManyToOne
    @JoinColumn(name = "id_statut_document")
    private DocumentStatus status;
    
    public Document() {}

    public Integer getId() { return id_document; }
    public void setId(Integer id) { this.id_document = id; }

    public File getFile() {
        return file;
    }

    public String getNom() { return nom_fichier; }
    public void setNom(String nom) { this.nom_fichier = nom; }

    public LocalDate getDateDocument() { return dateDocument; }
    public void setDateDocument(LocalDate dateDocument) { this.dateDocument = dateDocument; }

    public LocalDateTime getDateAjout() { return dateAjout; }
    public void setDateAjout(LocalDateTime dateAjout) { this.dateAjout = dateAjout; }

    //public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setTypeDocument(TypeDocument typeDocument) { this.typeDocument = typeDocument; }
    public TypeDocument getTypeDocument() { return typeDocument; }

    public Document(String nom, TypeDocument typeDocument, User user, LocalDate dateDocument) {
        this.nom_fichier = nom;
        this.typeDocument = typeDocument;
        this.user = user;
        this.dateDocument = dateDocument;
    }
}