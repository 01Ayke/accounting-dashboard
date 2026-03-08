package org.pfa.services;

import org.pfa.models.Compte;
import org.pfa.repositories.CompteRepository;

public class CompteService {
    private final CompteRepository repo = new CompteRepository();

    public void create(Compte compte) { repo.save(compte); }

     private Boolean exists(Compte compte) {
        return (repo.getByFileAndNumero(compte.getFile(), compte.getNumeroCompte()) != null);
    }

    // revoie true si le compte a été créé, false sinon
    public Boolean createIfNotExists(Compte compte) {
        Boolean exists = exists(compte);
        if (!exists) create(compte);
        return !exists;
    }

    public Compte find(Integer id) { return repo.findById(id); }
}
