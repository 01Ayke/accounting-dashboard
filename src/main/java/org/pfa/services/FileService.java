package org.pfa.services;

import org.pfa.models.Compte;
import org.pfa.models.File;
import org.pfa.repositories.CompteRepository;
import org.pfa.repositories.FileRepository;

import java.util.List;

public class FileService {
    private final FileRepository fileRepository = new FileRepository();
    private CompteRepository compteRepository = new CompteRepository();

    public List<File> getAll() { return fileRepository.findAll(); }
    public File getOne(Integer id) { return fileRepository.findById(id); }
    public void create(File file) { fileRepository.save(file); }

    public void deleteByName(String name) {
        File file  = fileRepository.findByName(name);
        for (Compte compte: file.getComptes()) {
            compteRepository.delete(compte);
        }
        fileRepository.deleteByName(name);
    }
}
