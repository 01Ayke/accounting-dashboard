package org.pfa.services;

import org.pfa.models.Journal;
import org.pfa.repositories.JournalRepository;

public class JournalService {
    private JournalRepository journalRepository = new JournalRepository();

    public void create(Journal journal) { journalRepository.save(journal); }
}
