package org.pfa.services;

import org.pfa.models.FormeJuridique;
import org.pfa.models.FormeJuridiqueRepository;

public class FormeJuridiqueService {
    private static final FormeJuridiqueRepository repo = new FormeJuridiqueRepository();

    public FormeJuridique fromCode(String code) { return repo.find(code); }
}
