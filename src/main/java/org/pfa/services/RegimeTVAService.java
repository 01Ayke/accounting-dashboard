package org.pfa.services;

import org.pfa.models.RegimeTVA;
import org.pfa.repositories.RegimeTVARepository;

import java.util.List;

public class RegimeTVAService {
    private static final RegimeTVARepository repo = new RegimeTVARepository();

    public RegimeTVA fromCode(String code) { return repo.findByCode(code); }
    public RegimeTVA getOne(Integer id) { return repo.findById(id); }
    public List<RegimeTVA> getAll() { return repo.findAll(); }
}
