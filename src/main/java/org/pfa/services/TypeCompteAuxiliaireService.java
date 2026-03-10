package org.pfa.services;

import org.pfa.models.TypeCompteAuxiliaire;
import org.pfa.repositories.TypeCompteAuxiliaireRepository;

public class TypeCompteAuxiliaireService {
    private static final TypeCompteAuxiliaireRepository repo = new TypeCompteAuxiliaireRepository();

    public TypeCompteAuxiliaire fromCode(String code) { return repo.find(code); }
}
