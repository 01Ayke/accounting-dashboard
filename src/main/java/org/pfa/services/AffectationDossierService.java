package org.pfa.services;

import org.pfa.models.AffectationDossier;
import org.pfa.models.File;
import org.pfa.models.User;
import org.pfa.models.dto.AffectationResponse;
import org.pfa.repositories.AffectationDossierRepository;
import org.pfa.repositories.FileRepository;
import org.pfa.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AffectationDossierService {
    private final AffectationDossierRepository affectationRepo = new AffectationDossierRepository();
    private final UserRepository userRepo = new UserRepository();
    private final FileRepository fileRepo = new FileRepository();

    public void attribuer(Integer idUser, Integer idDossier) throws Exception {
        AffectationDossier active = affectationRepo.findActiveByUserAndFile(idUser, idDossier);
        if (active != null) {
            throw new Exception("Ce dossier est déjà attribué à cet utilisateur.");
        }

        User user = userRepo.findById(idUser);
        if (user == null) throw new Exception("Utilisateur introuvable");

        File file = fileRepo.findById(idDossier);
        if (file == null) throw new Exception("Dossier introuvable");

        AffectationDossier affectation = new AffectationDossier(user, file, new Date(), true, true);
        affectationRepo.save(affectation);
    }

    public void desaffecter(Integer idUser, Integer idDossier) throws Exception {
        AffectationDossier active = affectationRepo.findActiveByUserAndFile(idUser, idDossier);
        if (active == null) {
            throw new Exception("Aucune affectation active trouvée pour ce dossier et cet utilisateur.");
        }

        active.setActif(false);
        active.setDateFinAffectation(new Date());
        affectationRepo.update(active);
    }

    public List<AffectationResponse> listerHistorique() {
        List<AffectationDossier> affectations = affectationRepo.findAll();
        List<AffectationResponse> responses = new ArrayList<>();
        
        for (AffectationDossier a : affectations) {
            AffectationResponse res = new AffectationResponse();
            res.setIdAffectation(a.getId());
            res.setIdUser(a.getUser().getId());
            res.setNomUser(a.getUser().getNom());
            res.setPrenomUser(a.getUser().getPrenom());
            res.setIdDossier(a.getFile().getId());
            res.setNomDossier(a.getFile().getDenominationSociale());
            res.setDateAffectation(a.getDateAffectation());
            res.setDateFinAffectation(a.getDateFinAffectation());
            res.setActif(a.getActif());
            responses.add(res);
        }
        return responses;
    }
}
