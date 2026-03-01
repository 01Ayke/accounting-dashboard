package org.pfa.services;

import org.pfa.models.File;
import org.pfa.models.GrandLivre;

import java.util.Date;

public class GrandLivreService {
    private FileService service = new FileService();

    public GrandLivre getOne(Long id, Date debut, Date fin) {
        File file = service.getOne(id);
        GrandLivre grandLivre = new GrandLivre(file, debut, fin);
        return grandLivre;
    }
}
