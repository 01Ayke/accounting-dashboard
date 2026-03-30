package org.pfa.services;

import org.pfa.models.ModeAmortissement;
import org.pfa.repositories.ModeAmortissementRepository;

import java.util.List;

public class ModeAmortissementService {
    private final ModeAmortissementRepository repo = new ModeAmortissementRepository();

    public List<ModeAmortissement> getAll() { return repo.findAll(); }
}
