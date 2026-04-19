package org.pfa.services;

import org.pfa.models.Journal;
import org.pfa.repositories.JournalRepository;
import java.util.List;

public class JournalService {
    private JournalRepository journalRepository = new JournalRepository();

    public void create(Journal journal) { journalRepository.save(journal); }

    public List<Journal> getByDossierId(Integer dossierId) {
        return journalRepository.findByDossierId(dossierId);
    }
}
