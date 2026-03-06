package org.pfa.services;

import org.pfa.models.Compte;
import org.pfa.repositories.CompteRepository;

public class CompteService {
    private final CompteRepository repo = new CompteRepository();

    public void create(Compte compte) { repo.save(compte); }
}
