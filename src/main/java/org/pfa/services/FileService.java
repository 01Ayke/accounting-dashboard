package org.pfa.services;

import org.pfa.models.Compte;
import org.pfa.models.CompteAuxiliaire;
import org.pfa.models.File;
import org.pfa.repositories.CompteAuxiliaireRepository;
import org.pfa.repositories.CompteRepository;
import org.pfa.repositories.FileRepository;

import java.util.List;
import java.util.Set;

public class FileService {
    private final FileRepository fileRepository = new FileRepository();
    private CompteRepository compteRepository = new CompteRepository();
    private CompteAuxiliaireRepository compteAuxiliaireRepository = new CompteAuxiliaireRepository();

    public List<File> getAll() { return fileRepository.findAll(); }
    public File getOne(Integer id) { return fileRepository.findById(id); }

    public File create(File file) {
        fileRepository.save(file);
        return file;
    }

    public void deleteByName(String name) {
        File file  = fileRepository.findByName(name);
        Set<Compte> comptes = file.getComptes();
        for (Compte compte: comptes) {
            for (CompteAuxiliaire compteAuxiliaire: compte.getComptesAuxiliaires()) {
                compteAuxiliaireRepository.delete(compteAuxiliaire.getIdCompteAuxiliaire());
            }
            compteRepository.delete(compte);
        }
        fileRepository.deleteByName(name);
    }
}
