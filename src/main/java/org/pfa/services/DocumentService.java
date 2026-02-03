package org.pfa.services;

import org.pfa.models.Document;
import org.pfa.repositories.DocumentRepository;
import java.util.List;

public class DocumentService {

    private final DocumentRepository repo = new DocumentRepository();
    
    public List<Document> getAll() { return repo.findAll(); }
    public Document getOne(Integer id) { return repo.findById(id);}
    public void create(Document doc) { repo.save(doc); }
    public void update(Document doc) { repo.update(doc);}
    public void delete(Integer id) { repo.delete(id); }
}