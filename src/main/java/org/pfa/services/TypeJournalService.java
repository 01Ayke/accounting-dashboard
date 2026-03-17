package org.pfa.services;

import org.pfa.models.TypeJournal;
import org.pfa.repositories.TypeJournalRepository;

import java.util.List;

public class TypeJournalService {
    private static final TypeJournalRepository repo = new TypeJournalRepository();

    public List<TypeJournal> getAll() { return repo.findAll(); }
    public TypeJournal getOne(Integer id) { return repo.findById(id); }
}
