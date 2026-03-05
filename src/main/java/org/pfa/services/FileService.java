package org.pfa.services;

import org.pfa.models.File;
import org.pfa.repositories.FileRepository;

import java.util.List;

public class FileService {
    private final FileRepository repo = new FileRepository();

    public List<File> getAll() { return repo.findAll(); }
    public File getOne(Long id) { return repo.findById(id); }
    public void create(File file) { repo.save(file); }
}
