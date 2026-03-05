package org.pfa.services;

import org.pfa.models.RegimeTVA;
import org.pfa.repositories.RegimeTVARepository;

public class RegimeTVAService {
    private static final RegimeTVARepository repo = new RegimeTVARepository();

    public RegimeTVA fromCode(String code) { return repo.find(code); }
}
