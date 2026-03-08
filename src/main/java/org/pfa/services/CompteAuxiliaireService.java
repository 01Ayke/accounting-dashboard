package org.pfa.services;

import org.pfa.models.CompteAuxiliaire;
import org.pfa.repositories.CompteAuxiliaireRepository;

public class CompteAuxiliaireService {
    private final CompteAuxiliaireRepository repo = new CompteAuxiliaireRepository();

    public void create(CompteAuxiliaire compteAuxiliaire) { repo.save(compteAuxiliaire); }

    private Boolean exists(CompteAuxiliaire compteAuxiliaire) {
        return (repo.getByCompteAndLibelle(compteAuxiliaire.getCompte(), compteAuxiliaire.getNumeroCompteAuxiliaire()) != null);
    }

    public Boolean createIfNotExists(CompteAuxiliaire compteAuxiliaire) {
        Boolean exists = exists(compteAuxiliaire);
        if (!exists) create(compteAuxiliaire);
        return !exists;
    }
}
