package org.pfa.services;

import org.pfa.models.FormeJuridique;
import org.pfa.repositories.FormeJuridiqueRepository;

import java.util.List;

public class FormeJuridiqueService {
    private static final FormeJuridiqueRepository repo = new FormeJuridiqueRepository();

    public FormeJuridique fromCode(String code) { return repo.findByCode(code); }
    public FormeJuridique getOne(Integer id) { return repo.findById(id); }
    public List<FormeJuridique> getAll() { return repo.findAll(); }
}
