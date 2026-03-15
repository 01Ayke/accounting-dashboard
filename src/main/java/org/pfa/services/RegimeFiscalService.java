package org.pfa.services;

import org.pfa.models.RegimeFiscal;
import org.pfa.repositories.RegimeFiscalRepository;

import java.util.List;

public class RegimeFiscalService {
    private final RegimeFiscalRepository repo = new RegimeFiscalRepository();

    public RegimeFiscal fromCode(String code) { return repo.findByCode(code); }
    public RegimeFiscal getOne(Integer id) { return repo.findById(id); }
    public List<RegimeFiscal> getAll() { return repo.findAll(); }
}
