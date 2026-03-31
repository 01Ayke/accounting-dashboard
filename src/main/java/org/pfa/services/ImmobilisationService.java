package org.pfa.services;

import org.pfa.models.Immobilisation;
import org.pfa.repositories.ImmobilisationRepository;

public class ImmobilisationService {
    private final ImmobilisationRepository repo = new ImmobilisationRepository();

    public void create(Immobilisation immobilisation) {
        repo.save(immobilisation);
    }
}
