package org.pfa.services;

import org.pfa.models.Address;
import org.pfa.repositories.AdresseRepository;

public class AdresseService {
    private final AdresseRepository repo = new AdresseRepository();

    public Address getOne(String pays, String ville, String rue, String numero, String complement, String codePostal) {
        return repo.find(pays, ville, rue, numero, complement, codePostal);
    }

    public Boolean exists(Address adresse) {
        return (matching(adresse) != null);
    }

    public Address matching(Address adresse) {
        return getOne(adresse.getPays(), adresse.getVille(), adresse.getRue(), adresse.getNumero(), adresse.getComplement(), adresse.getCodePostal());
    }

    public Address create(Address adresse) {
        repo.save(adresse);
        return adresse;
    }

    // ajoute l'adresse dans la base si elle n'y est pas, et la renvoie
    public Address createOrGet(Address adresse) {
        Address matchingAddress = matching(adresse);
        if (matchingAddress == null) {
            return create(adresse);
        }
        else {
            return matchingAddress;
        }
    }
}
