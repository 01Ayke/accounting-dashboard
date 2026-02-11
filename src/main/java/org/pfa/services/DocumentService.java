package org.pfa.services;

import org.pfa.models.*;
import org.pfa.repositories.*;
import java.time.LocalDate;
import java.util.List;

public class DocumentService {

    private final DocumentRepository documentRepository = new DocumentRepository();
    private final UserRepository userRepository = new UserRepository();
    private final TypeDocumentRepository typeDocumentRepository = new TypeDocumentRepository();

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document getDocumentById(Integer id) {
        return documentRepository.findById(id);
    }

    public Document createDocument(String nom, Integer typeId, LocalDate dateDocument, Integer userId) {
        User user = userRepository.findById(userId);
        TypeDocument type = typeDocumentRepository.findById(typeId);
        if (user == null) {
            throw new RuntimeException("Utilisateur introuvable pour l'ID " + userId);
        }
        if (type == null) {
            throw new RuntimeException("Type de document introuvable pour l'ID " + typeId);
        }

        if (dateDocument == null) { dateDocument = LocalDate.now();}
        Document doc = new Document(nom, type, user, dateDocument);
        documentRepository.save(doc);
        return doc;
    }

    public Document updateDocument(Integer docId, String nouveauNom, Integer nouveauTypeId, LocalDate nouvelleDate) {
        Document doc = documentRepository.findById(docId);
        if (doc == null) {
            throw new RuntimeException("Document introuvable pour l'ID " + docId);
        }
        
        if (nouveauNom != null) doc.setNom(nouveauNom);
        if (nouveauTypeId != null) {
            TypeDocument newType = typeDocumentRepository.findById(nouveauTypeId);
            if (newType == null) {
                throw new RuntimeException("Type de document introuvable pour l'ID " + nouveauTypeId);
            }
            doc.setTypeDocument(newType);
        }
        if (nouvelleDate != null) doc.setDateDocument(nouvelleDate);

        documentRepository.update(doc);
        return doc;
    }

    public void deleteDocument(Integer id) {
        Document doc = documentRepository.findById(id);
        if (doc == null) {
            throw new RuntimeException("Impossible de supprimer : Document introuvable");
        }
        documentRepository.delete(id);
    }
}