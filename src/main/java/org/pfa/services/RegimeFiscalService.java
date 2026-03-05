package org.pfa.services;

import org.pfa.models.RegimeFiscal;
import org.pfa.repositories.RegimeFiscalRepository;

public class RegimeFiscalService {
    private final RegimeFiscalRepository repo = new RegimeFiscalRepository();

    public RegimeFiscal fromCode(String code) { return repo.find(code); }
}
