package org.pfa.services;

import org.pfa.models.Financement;
import org.pfa.repositories.FinancementRepository;

public class FinancementService {
    private final FinancementRepository repo = new FinancementRepository();

    public void create(Financement financement) {
        repo.save(financement);
    }
}
