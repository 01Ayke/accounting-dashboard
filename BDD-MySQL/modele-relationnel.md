# Modèle relationnel — Base `lakonta`

> **Convention :**
> - `PK` = clé primaire · `FK` = clé étrangère
> - Les **FK** sont notées en *italique* avec `#` devant le nom

---

## Tables de référence

**type_journal**(`PK` id_type_journal, libelle, code)

**forme_juridique**(`PK` id_forme_juridique, libelle, code)

**regime_fiscal**(`PK` id_regime_fiscal, libelle, code)

**regime_tva**(`PK` id_regime_tva, libelle, code)

**mode_remboursement**(`PK` id_mode_remboursement, libelle, code)

**mode_amortissement**(`PK` id_mode_amortissement, libelle, code)

**type_immobilisation**(`PK` id_type_immobilisation, libelle, code)

**type_compte_auxiliaire**(`PK` id_type_compte_auxiliaire, libelle, code)

**type_devise**(`PK` id_type_devise, code, libelle, symbole)

**role_utilisateur**(`PK` id_role, libelle, code, description, niveau_acces, actif)

**type_action**(`PK` id_type_action, libelle, code, categorie, description, niveau_importance)

**statut_document**(`PK` id_statut_document, libelle, code, description)

---

## Tables auto-référencées

**categorie_document**(`PK` id_categorie_document, libelle, code, description, *`FK` #id_categorie_parent*, actif)

**type_document**(`PK` id_type_document, libelle, code, *`FK` #id_categorie_document*, conservation_annees, obligatoire, description, actif)

---

## Niveau 1 — Tables de base

**adresse**(`PK` id_adresse, pays, ville, rue, numero, complement, code_postal)

**utilisateur**(`PK` id_utilisateur, mail, nom, prenom, code, mot_de_passe, date_creation, date_derniere_connexion, actif)

---

## Niveau 2

**representant**(`PK` id_representant, telephone, mail, nom, prenom, *`FK` #id_adresse*, qualite, date_creation, date_modification, actif)

**dossier**(`PK` id_dossier, telephone, mail, denomination_sociale, *`FK` #id_forme_juridique*, *`FK` #id_regime_fiscal*, *`FK` #id_regime_tva*, capital, *`FK` #id_adresse*, commentaire, date_creation, date_modification, actif)

**utilisateur_role**(*`FK` #id_utilisateur*, *`FK` #id_role*, date_attribution, date_expiration)

---

## Niveau 3

**dossier_representant**(*`FK` #id_dossier*, *`FK` #id_representant*, date_debut, date_fin, principal)

**comptable_dossier**(`PK` id_comptable_dossier, *`FK` #id_utilisateur*, *`FK` #id_dossier*, date_attribution, date_fin_attribution, responsable_principal, pourcentage_charge, commentaire, actif)

**journal**(`PK` id_journal, *`FK` #id_dossier*, *`FK` #id_type_journal*, libelle, code, commentaire, solde, actif)

**compte**(`PK` id_compte, numero_compte, libelle, *`FK` #id_dossier*, solde, actif)

**session_travail**(`PK` id_session, *`FK` #id_utilisateur*, *`FK` #id_dossier*, date_debut, date_fin, duree_minutes, nombre_actions, type_travail, commentaire)

**communication**(`PK` id_communication, *`FK` #id_utilisateur*, *`FK` #id_representant*, *`FK` #id_dossier*, type_communication, objet, contenu, date_communication, duree_minutes, sens, necessite_suivi, date_suivi_prevu, suivi_effectue, date_suivi_effectue, urgent, confidentiel)

---

## Niveau 4

**financement**(`PK` id_financement, date_financement, duree_mois, montant, taux, *`FK` #id_mode_remboursement*, date_premiere_echeance, remboursement_total, cout_assurance, montant_interet_premiere_echeance, commentaire, *`FK` #id_compte*, etablissement, intitule, actif)

**compte_auxiliaire**(`PK` id_compte_auxiliaire, *`FK` #id_compte_rattachement*, libelle, numero_compte_auxiliaire, *`FK` #id_type_compte_auxiliaire*, solde, actif)

**document**(`PK` id_document, nom_fichier, nom_original, chemin_stockage, extension, taille_octets, hash_md5, hash_sha256, mime_type, *`FK` #id_type_document*, *`FK` #id_statut_document*, *`FK` #id_dossier*, titre, description, mots_cles, date_document, date_telechargement, *`FK` #id_utilisateur_telechargement*, numero_version, *`FK` #id_document_parent*, date_archivage, date_suppression, *`FK` #id_utilisateur_suppression*, motif_suppression, confidentiel, actif)

**ecriture_comptable**(`PK` id_ecriture_comptable, date_saisie, date_validation, *`FK` #id_operateur*, code_operation, debit, credit, *`FK` #id_compte*, *`FK` #id_compte_auxiliaire*, numero_facture, libelle, date_facture, *`FK` #id_journal*, lettrage, date_lettrage, montant_devise, *`FK` #id_type_devise*)

---

## Niveau 5

**immobilisation**(`PK` id_immobilisation, date_acquisition, cout_acquisition, duree_amortissement_economique, *`FK` #id_mode_amortissement_economique*, base_amortissement_economique, *`FK` #id_financement*, duree_amortissement_fiscal, *`FK` #id_mode_amortissement_fiscal*, base_amortissement_fiscal, *`FK` #id_compte_immobilisation*, commentaires, *`FK` #id_type_immobilisation*, intitule, actif)

**log_interaction**(`PK` id_log, *`FK` #id_utilisateur*, *`FK` #id_representant*, *`FK` #id_type_action*, description, *`FK` #id_dossier*, *`FK` #id_ecriture_comptable*, *`FK` #id_document*, *`FK` #id_immobilisation*, *`FK` #id_financement*, *`FK` #id_compte*, *`FK` #id_journal*, date_action, duree_secondes, adresse_ip, user_agent, ancienne_valeur, nouvelle_valeur, contexte, commentaire_utilisateur)

---

## Tables de liaison

**document_ecriture**(*`FK` #id_document*, *`FK` #id_ecriture_comptable*)

**document_immobilisation**(*`FK` #id_document*, *`FK` #id_immobilisation*)

**document_financement**(*`FK` #id_document*, *`FK` #id_financement*)

**document_representant**(*`FK` #id_document*, *`FK` #id_representant*)

**communication_document**(*`FK` #id_communication*, *`FK` #id_document*)

---

## Tables annexes

**historique_document**(`PK` id_historique_document, *`FK` #id_document*, *`FK` #id_utilisateur*, action, date_action, details, adresse_ip)

**droit_acces_document**(`PK` id_droit_acces, *`FK` #id_document*, *`FK` #id_utilisateur*, lecture, modification, suppression, date_attribution, date_expiration)

**commentaire_document**(`PK` id_commentaire, *`FK` #id_document*, *`FK` #id_utilisateur*, commentaire, date_commentaire, *`FK` #id_commentaire_parent*)

**notification**(`PK` id_notification, *`FK` #id_utilisateur_destinataire*, *`FK` #id_utilisateur_emetteur*, titre, message, type_notification, *`FK` #id_log_interaction*, *`FK` #id_communication*, *`FK` #id_dossier*, date_creation, date_lecture, lue, priorite)