package org.pfa.services;

import org.pfa.models.Compte;
import org.pfa.models.File;
import org.pfa.models.dto.CompteLight;
import org.pfa.repositories.CompteRepository;

import java.util.List;

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

    public Compte findByFileAndNumero(File file, String numero) {
        return repo.getByFileAndNumero(file, numero);
    }

    public List<Compte> findByFile(File file) { return repo.getByFile(file); }

    public List<CompteLight> findByFileLight(File file) { return repo.getByFileLight(file); }

    public List<Compte> getByFileMatchingExp(File file, String exp) {
        List<Compte> comptes = findByFile(file);
        return comptes.stream().filter(compte -> compte.getNumeroCompte().matches(exp)).toList();
    }
}
