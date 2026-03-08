DROP DATABASE IF EXISTS lakonta;

-- -----------------------------------------------------
-- Schema lakonta
-- -----------------------------------------------------
CREATE DATABASE lakonta CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE lakonta;

-- -----------------------------------------------------
-- Tables de référence (sans dépendances)
-- -----------------------------------------------------

-- Types de journaux
CREATE TABLE type_journal (
  id_type_journal INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  libelle VARCHAR(45) NOT NULL UNIQUE,
  code VARCHAR(10) NOT NULL UNIQUE
);

-- Formes juridiques
CREATE TABLE forme_juridique (
  id_forme_juridique INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  libelle VARCHAR(45) NOT NULL UNIQUE,
  code VARCHAR(10) NOT NULL UNIQUE
);

-- Régimes fiscaux
CREATE TABLE regime_fiscal (
  id_regime_fiscal INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  libelle VARCHAR(45) NOT NULL UNIQUE,
  code VARCHAR(10) NOT NULL UNIQUE
);

-- Régimes TVA
CREATE TABLE regime_tva (
  id_regime_tva INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  libelle VARCHAR(45) NOT NULL UNIQUE,
  code VARCHAR(10) NOT NULL UNIQUE
);

-- Modes de remboursement
CREATE TABLE mode_remboursement (
  id_mode_remboursement INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  libelle VARCHAR(45) NOT NULL UNIQUE,
  code VARCHAR(10) NOT NULL UNIQUE
);

-- Modes d'amortissement
CREATE TABLE mode_amortissement (
  id_mode_amortissement INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  libelle VARCHAR(45) NOT NULL UNIQUE,
  code VARCHAR(10) NOT NULL UNIQUE
);

-- Types d'immobilisation
CREATE TABLE type_immobilisation (
  id_type_immobilisation INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  libelle VARCHAR(45) NOT NULL UNIQUE,
  code VARCHAR(10) NOT NULL UNIQUE
);

-- Types de compte auxiliaire
CREATE TABLE type_compte_auxiliaire (
  id_type_compte_auxiliaire INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  libelle VARCHAR(45) NOT NULL UNIQUE,
  code VARCHAR(10) NOT NULL UNIQUE
);

-- Types de devises
CREATE TABLE type_devise (
  id_type_devise INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  code VARCHAR(3) NOT NULL UNIQUE,
  libelle VARCHAR(45) NOT NULL,
  symbole VARCHAR(5)
);

-- Rôles utilisateurs
CREATE TABLE role_utilisateur (
  id_role INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  libelle VARCHAR(45) NOT NULL UNIQUE,
  code VARCHAR(20) NOT NULL UNIQUE,
  description TEXT NULL,
  niveau_acces INT NOT NULL DEFAULT 1 CHECK (niveau_acces BETWEEN 1 AND 10),
  actif BOOLEAN NOT NULL DEFAULT TRUE
);

-- Types d'actions pour le logging
CREATE TABLE type_action (
  id_type_action INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  libelle VARCHAR(80) NOT NULL UNIQUE,
  code VARCHAR(30) NOT NULL UNIQUE,
  categorie VARCHAR(45) NOT NULL,
  description TEXT NULL,
  niveau_importance ENUM('INFO', 'WARNING', 'CRITICAL') NOT NULL DEFAULT 'INFO'
);

-- Catégories de documents (auto-référencée)
CREATE TABLE categorie_document (
  id_categorie_document INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  libelle VARCHAR(80) NOT NULL UNIQUE,
  code VARCHAR(20) NOT NULL UNIQUE,
  description TEXT NULL,
  id_categorie_parent INT NULL,
  actif BOOLEAN NOT NULL DEFAULT TRUE,

  FOREIGN KEY (id_categorie_parent) REFERENCES categorie_document (id_categorie_document) ON DELETE SET NULL,

  INDEX idx_code (code),
  INDEX idx_parent (id_categorie_parent)
);

-- Types de documents
CREATE TABLE type_document (
  id_type_document INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  libelle VARCHAR(80) NOT NULL UNIQUE,
  code VARCHAR(20) NOT NULL UNIQUE,
  id_categorie_document INT NOT NULL,
  conservation_annees INT NULL,
  obligatoire BOOLEAN NOT NULL DEFAULT FALSE,
  description TEXT NULL,
  actif BOOLEAN NOT NULL DEFAULT TRUE,

  FOREIGN KEY (id_categorie_document) REFERENCES categorie_document (id_categorie_document) ON DELETE RESTRICT,

  INDEX idx_code (code),
  INDEX idx_categorie (id_categorie_document)
);

-- Statuts de document
CREATE TABLE statut_document (
  id_statut_document INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  libelle VARCHAR(45) NOT NULL UNIQUE,
  code VARCHAR(20) NOT NULL UNIQUE,
  description TEXT NULL
);

-- -----------------------------------------------------
-- Tables de base (niveau 1)
-- -----------------------------------------------------

-- Table adresse
CREATE TABLE adresse (
  id_adresse INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  pays VARCHAR(45) NOT NULL,
  ville VARCHAR(45) NOT NULL,
  rue VARCHAR(45) NOT NULL,
  numero VARCHAR(10),
  complement VARCHAR(90),
  code_postal VARCHAR(10),

  INDEX idx_ville (ville),
  INDEX idx_pays (pays)
);

-- Table utilisateur
CREATE TABLE utilisateur (
  id_utilisateur INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  mail VARCHAR(80) NOT NULL,
  nom VARCHAR(45) NOT NULL,
  prenom VARCHAR(45) NOT NULL,
  code VARCHAR(45) NOT NULL,
  mot_de_passe VARCHAR(255) NOT NULL,
  date_creation DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_derniere_connexion DATETIME NULL,
  actif BOOLEAN NOT NULL DEFAULT TRUE,

  UNIQUE KEY uk_mail (mail),
  UNIQUE KEY uk_code (code),

  INDEX idx_mail (mail),
  INDEX idx_code (code)
);

-- -----------------------------------------------------
-- Tables de niveau 2 (dépendent de niveau 1)
-- -----------------------------------------------------

-- Table representant
CREATE TABLE representant (
  id_representant INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  telephone VARCHAR(20) NOT NULL,
  mail VARCHAR(80) NOT NULL,
  nom VARCHAR(45) NOT NULL,
  prenom VARCHAR(45) NOT NULL,
  id_adresse INT NOT NULL,
  qualite VARCHAR(45) NOT NULL,
  date_creation DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_modification DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  actif BOOLEAN NOT NULL DEFAULT TRUE,

  UNIQUE KEY uk_mail (mail),
  FOREIGN KEY (id_adresse) REFERENCES adresse (id_adresse) ON DELETE RESTRICT,

  INDEX idx_nom_prenom (nom, prenom),
  INDEX idx_mail (mail)
);

-- Table dossier
CREATE TABLE dossier (
  id_dossier INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  telephone VARCHAR(20) NOT NULL,
  mail VARCHAR(80) NOT NULL,
  denomination_sociale VARCHAR(80) NOT NULL,
  id_forme_juridique INT NOT NULL,
  id_regime_fiscal INT NOT NULL,
  id_regime_tva INT NOT NULL,
  capital DECIMAL(15,2) CHECK (capital >= 0),
  id_adresse INT NOT NULL,
  commentaire TEXT NULL,
  date_creation DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_modification DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  actif BOOLEAN NOT NULL DEFAULT TRUE,

  UNIQUE KEY uk_denomination (denomination_sociale),
  FOREIGN KEY (id_adresse) REFERENCES adresse (id_adresse) ON DELETE RESTRICT,
  FOREIGN KEY (id_forme_juridique) REFERENCES forme_juridique (id_forme_juridique),
  FOREIGN KEY (id_regime_fiscal) REFERENCES regime_fiscal (id_regime_fiscal),
  FOREIGN KEY (id_regime_tva) REFERENCES regime_tva (id_regime_tva),

  INDEX idx_denomination (denomination_sociale),
  INDEX idx_mail (mail)
);

-- Table de liaison utilisateur-rôle
CREATE TABLE utilisateur_role (
  id_utilisateur INT NOT NULL,
  id_role INT NOT NULL,
  date_attribution DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_expiration DATETIME NULL,

  PRIMARY KEY (id_utilisateur, id_role),
  FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id_utilisateur) ON DELETE CASCADE,
  FOREIGN KEY (id_role) REFERENCES role_utilisateur (id_role) ON DELETE CASCADE,

  INDEX idx_role (id_role),
  INDEX idx_expiration (date_expiration)
);

-- -----------------------------------------------------
-- Tables de niveau 3 (dépendent de niveau 2)
-- -----------------------------------------------------

-- Table de liaison dossier_representant (Many-to-Many)
CREATE TABLE dossier_representant (
  id_dossier INT NOT NULL,
  id_representant INT NOT NULL,
  date_debut DATE NOT NULL,
  date_fin DATE NULL,
  principal BOOLEAN NOT NULL DEFAULT FALSE,

  PRIMARY KEY (id_dossier, id_representant),
  FOREIGN KEY (id_dossier) REFERENCES dossier (id_dossier) ON DELETE CASCADE,
  FOREIGN KEY (id_representant) REFERENCES representant (id_representant) ON DELETE CASCADE,

  CHECK (date_fin IS NULL OR date_fin >= date_debut),
  INDEX idx_date_debut (date_debut),
  INDEX idx_representant (id_representant)
);

-- Table d'attribution des dossiers aux comptables
CREATE TABLE comptable_dossier (
  id_comptable_dossier INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  id_utilisateur INT NOT NULL,
  id_dossier INT NOT NULL,
  date_attribution DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_fin_attribution DATETIME NULL,
  responsable_principal BOOLEAN NOT NULL DEFAULT TRUE,
  pourcentage_charge INT NULL CHECK (pourcentage_charge BETWEEN 0 AND 100),
  commentaire TEXT NULL,
  actif BOOLEAN NOT NULL DEFAULT TRUE,

  UNIQUE KEY uk_utilisateur_dossier_actif (id_utilisateur, id_dossier, actif),
  FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id_utilisateur) ON DELETE RESTRICT,
  FOREIGN KEY (id_dossier) REFERENCES dossier (id_dossier) ON DELETE CASCADE,

  CHECK (date_fin_attribution IS NULL OR date_fin_attribution >= date_attribution),

  INDEX idx_utilisateur (id_utilisateur),
  INDEX idx_dossier (id_dossier),
  INDEX idx_date_attribution (date_attribution),
  INDEX idx_responsable (responsable_principal)
);

-- Table journal
CREATE TABLE journal (
  id_journal INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  id_dossier INT NOT NULL,
  id_type_journal INT NOT NULL,
  libelle VARCHAR(45) NOT NULL,
  code VARCHAR(45) NOT NULL,
  commentaire TEXT NULL,
  solde DECIMAL(15,2) NOT NULL DEFAULT 0,
  actif BOOLEAN NOT NULL DEFAULT TRUE,

  UNIQUE KEY uk_dossier_code (id_dossier, code),
  FOREIGN KEY (id_dossier) REFERENCES dossier (id_dossier) ON DELETE RESTRICT,
  FOREIGN KEY (id_type_journal) REFERENCES type_journal (id_type_journal),

  INDEX idx_code (code),
  INDEX idx_dossier (id_dossier)
);

-- Table compte
CREATE TABLE compte (
  id_compte INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  numero_compte VARCHAR(20) NOT NULL,
  libelle VARCHAR(120) NOT NULL,
  id_dossier INT NOT NULL,
  solde DECIMAL(15,2) NOT NULL DEFAULT 0,
  actif BOOLEAN NOT NULL DEFAULT TRUE,

  UNIQUE KEY uk_dossier_numero (id_dossier, numero_compte),
  FOREIGN KEY (id_dossier) REFERENCES dossier (id_dossier) ON DELETE RESTRICT,

  INDEX idx_numero_compte (numero_compte),
  INDEX idx_dossier (id_dossier)
);

-- Table sessions de travail
CREATE TABLE session_travail (
  id_session INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  id_utilisateur INT NOT NULL,
  id_dossier INT NOT NULL,
  date_debut DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_fin DATETIME NULL,
  duree_minutes INT NULL,
  nombre_actions INT NOT NULL DEFAULT 0,
  type_travail VARCHAR(45) NULL,
  commentaire TEXT NULL,

  FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id_utilisateur) ON DELETE RESTRICT,
  FOREIGN KEY (id_dossier) REFERENCES dossier (id_dossier) ON DELETE CASCADE,

  INDEX idx_utilisateur (id_utilisateur),
  INDEX idx_dossier (id_dossier),
  INDEX idx_date_debut (date_debut),
  INDEX idx_date_fin (date_fin)
);

-- Table des communications client-comptable
CREATE TABLE communication (
  id_communication INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  id_utilisateur INT NOT NULL,
  id_representant INT NULL,
  id_dossier INT NOT NULL,

  type_communication ENUM('EMAIL', 'TELEPHONE', 'VISIO', 'RENCONTRE', 'COURRIER', 'AUTRE') NOT NULL,

  objet VARCHAR(200) NOT NULL,
  contenu TEXT NOT NULL,

  date_communication DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  duree_minutes INT NULL,

  sens ENUM('ENTRANT', 'SORTANT') NOT NULL,

  necessite_suivi BOOLEAN NOT NULL DEFAULT FALSE,
  date_suivi_prevu DATETIME NULL,
  suivi_effectue BOOLEAN NOT NULL DEFAULT FALSE,
  date_suivi_effectue DATETIME NULL,

  urgent BOOLEAN NOT NULL DEFAULT FALSE,
  confidentiel BOOLEAN NOT NULL DEFAULT FALSE,

  FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id_utilisateur) ON DELETE RESTRICT,
  FOREIGN KEY (id_representant) REFERENCES representant (id_representant) ON DELETE SET NULL,
  FOREIGN KEY (id_dossier) REFERENCES dossier (id_dossier) ON DELETE CASCADE,

  INDEX idx_utilisateur (id_utilisateur),
  INDEX idx_representant (id_representant),
  INDEX idx_dossier (id_dossier),
  INDEX idx_date (date_communication),
  INDEX idx_suivi (necessite_suivi, suivi_effectue),
  INDEX idx_type (type_communication),
  FULLTEXT INDEX idx_recherche (objet, contenu)
);

-- -----------------------------------------------------
-- Tables de niveau 4 (dépendent de niveau 3)
-- -----------------------------------------------------

-- Table financement
CREATE TABLE financement (
    id_financement INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    date_financement DATE NOT NULL,
    duree_mois INT NOT NULL CHECK (duree_mois > 0),
    montant DECIMAL(15,2) NOT NULL CHECK (montant > 0),
    taux DECIMAL(5,2) NOT NULL CHECK (taux >= 0 AND taux <= 100),
    id_mode_remboursement INT NOT NULL,
    date_premiere_echeance DATE NOT NULL,
    remboursement_total DECIMAL(15,2) NOT NULL CHECK (remboursement_total >= 0),
    cout_assurance DECIMAL(15,2) NOT NULL DEFAULT 0 CHECK (cout_assurance >= 0),
    montant_interet_premiere_echeance DECIMAL(15,2) NOT NULL CHECK (montant_interet_premiere_echeance >= 0),
    commentaire TEXT NULL,
    id_compte INT NOT NULL,
    etablissement VARCHAR(80) NOT NULL,
    intitule VARCHAR(80) NOT NULL,
    actif BOOLEAN NOT NULL DEFAULT TRUE,

    FOREIGN KEY (id_compte) REFERENCES compte (id_compte) ON DELETE RESTRICT,
    FOREIGN KEY (id_mode_remboursement) REFERENCES mode_remboursement (id_mode_remboursement),

    INDEX idx_etablissement (etablissement),
    INDEX idx_date (date_financement)
);

-- Trigger pour s'assurer que remboursement_total >= montant
DELIMITER //

CREATE TRIGGER financement_before_insert
BEFORE INSERT ON financement
FOR EACH ROW
BEGIN
    IF NEW.remboursement_total < NEW.montant THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'remboursement_total doit etre >= montant';
    END IF;
END;
//

CREATE TRIGGER financement_before_update
BEFORE UPDATE ON financement
FOR EACH ROW
BEGIN
    IF NEW.remboursement_total < NEW.montant THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'remboursement_total doit etre >= montant';
    END IF;
END;
//

DELIMITER ;

-- Table compte_auxiliaire
CREATE TABLE compte_auxiliaire (
  id_compte_auxiliaire INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  id_compte_rattachement INT NOT NULL,
  libelle VARCHAR(80) NOT NULL,
  numero_compte_auxiliaire VARCHAR(20) NOT NULL,
  id_type_compte_auxiliaire INT NOT NULL,
  solde DECIMAL(15,2) NOT NULL DEFAULT 0,
  actif BOOLEAN NOT NULL DEFAULT TRUE,

  UNIQUE KEY uk_compte_numero (id_compte_rattachement, numero_compte_auxiliaire),
  FOREIGN KEY (id_compte_rattachement) REFERENCES compte (id_compte) ON DELETE RESTRICT,
  FOREIGN KEY (id_type_compte_auxiliaire) REFERENCES type_compte_auxiliaire (id_type_compte_auxiliaire),

  INDEX idx_compte_rattachement (id_compte_rattachement),
  INDEX idx_type (id_type_compte_auxiliaire)
);

-- Table document
CREATE TABLE document (
  id_document INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  nom_fichier VARCHAR(255) NOT NULL,
  nom_original VARCHAR(255) NOT NULL,
  chemin_stockage VARCHAR(500) NOT NULL,
  extension VARCHAR(10) NOT NULL,
  taille_octets BIGINT NOT NULL CHECK (taille_octets > 0),
  hash_md5 VARCHAR(32) NOT NULL,
  hash_sha256 VARCHAR(64) NOT NULL,
  mime_type VARCHAR(100) NOT NULL,

  id_type_document INT NOT NULL,
  id_statut_document INT NOT NULL,
  id_dossier INT NOT NULL,

  titre VARCHAR(200) NOT NULL,
  description TEXT NULL,
  mots_cles VARCHAR(500) NULL,

  date_document DATE NULL,
  date_telechargement DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  id_utilisateur_telechargement INT NOT NULL,

  numero_version INT NOT NULL DEFAULT 1,
  id_document_parent INT NULL,

  date_archivage DATETIME NULL,
  date_suppression DATETIME NULL,
  id_utilisateur_suppression INT NULL,
  motif_suppression TEXT NULL,

  confidentiel BOOLEAN NOT NULL DEFAULT FALSE,
  actif BOOLEAN NOT NULL DEFAULT TRUE,

  FOREIGN KEY (id_type_document) REFERENCES type_document (id_type_document) ON DELETE RESTRICT,
  FOREIGN KEY (id_statut_document) REFERENCES statut_document (id_statut_document) ON DELETE RESTRICT,
  FOREIGN KEY (id_dossier) REFERENCES dossier (id_dossier) ON DELETE RESTRICT,
  FOREIGN KEY (id_utilisateur_telechargement) REFERENCES utilisateur (id_utilisateur) ON DELETE RESTRICT,
  FOREIGN KEY (id_utilisateur_suppression) REFERENCES utilisateur (id_utilisateur) ON DELETE RESTRICT,
  FOREIGN KEY (id_document_parent) REFERENCES document (id_document) ON DELETE SET NULL,

  INDEX idx_nom_fichier (nom_fichier),
  INDEX idx_hash_md5 (hash_md5),
  INDEX idx_hash_sha256 (hash_sha256),
  INDEX idx_type (id_type_document),
  INDEX idx_statut (id_statut_document),
  INDEX idx_dossier (id_dossier),
  INDEX idx_date_document (date_document),
  INDEX idx_date_telechargement (date_telechargement),
  INDEX idx_version (numero_version),
  FULLTEXT INDEX idx_recherche (titre, description, mots_cles)
);

-- Table ecriture_comptable
CREATE TABLE ecriture_comptable (
  id_ecriture_comptable INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  date_saisie DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_validation DATETIME NULL,
  id_operateur INT NOT NULL,
  code_operation VARCHAR(45) NOT NULL,
  debit DECIMAL(15,2) NULL CHECK (debit IS NULL OR debit >= 0),
  credit DECIMAL(15,2) NULL CHECK (credit IS NULL OR credit >= 0),
  id_compte INT NOT NULL,
  id_compte_auxiliaire INT NULL,
  numero_facture VARCHAR(45) NULL,
  libelle VARCHAR(120) NULL,
  date_facture DATE NULL,
  id_journal INT NOT NULL,
  lettrage VARCHAR(10) NULL,
  date_lettrage DATE NULL,
  montant_devise DECIMAL(15,2) NULL,
  id_type_devise INT NULL,

  CONSTRAINT chk_debit_credit CHECK (
    (debit IS NOT NULL AND credit IS NULL) OR
    (debit IS NULL AND credit IS NOT NULL)
  ),

  FOREIGN KEY (id_operateur) REFERENCES utilisateur (id_utilisateur) ON DELETE RESTRICT,
  FOREIGN KEY (id_compte) REFERENCES compte (id_compte) ON DELETE RESTRICT,
  FOREIGN KEY (id_compte_auxiliaire) REFERENCES compte_auxiliaire (id_compte_auxiliaire) ON DELETE RESTRICT,
  FOREIGN KEY (id_journal) REFERENCES journal (id_journal) ON DELETE RESTRICT,
  FOREIGN KEY (id_type_devise) REFERENCES type_devise (id_type_devise),

  INDEX idx_date_saisie (date_saisie),
  INDEX idx_date_validation (date_validation),
  INDEX idx_operateur (id_operateur),
  INDEX idx_journal (id_journal),
  INDEX idx_compte (id_compte),
  INDEX idx_lettrage (lettrage),
  INDEX idx_numero_facture (numero_facture)
);

-- -----------------------------------------------------
-- Tables de niveau 5 (dépendent de niveau 4)
-- -----------------------------------------------------

-- Table immobilisation
CREATE TABLE immobilisation (
  id_immobilisation INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  date_acquisition DATE NOT NULL,
  cout_acquisition DECIMAL(15,2) NOT NULL CHECK (cout_acquisition > 0),
  duree_amortissement_economique INT NOT NULL CHECK (duree_amortissement_economique > 0),
  id_mode_amortissement_economique INT NOT NULL,
  base_amortissement_economique DECIMAL(15,2) NOT NULL CHECK (base_amortissement_economique > 0),
  id_financement INT NULL,
  duree_amortissement_fiscal INT NULL CHECK (duree_amortissement_fiscal > 0),
  id_mode_amortissement_fiscal INT NULL,
  base_amortissement_fiscal DECIMAL(15,2) NULL CHECK (base_amortissement_fiscal > 0),
  id_compte_immobilisation INT NOT NULL,
  commentaires TEXT NULL,
  id_type_immobilisation INT NOT NULL,
  intitule VARCHAR(80) NOT NULL,
  actif BOOLEAN NOT NULL DEFAULT TRUE,

  FOREIGN KEY (id_compte_immobilisation) REFERENCES compte (id_compte) ON DELETE RESTRICT,
  FOREIGN KEY (id_financement) REFERENCES financement (id_financement) ON DELETE SET NULL,
  FOREIGN KEY (id_mode_amortissement_economique) REFERENCES mode_amortissement (id_mode_amortissement),
  FOREIGN KEY (id_mode_amortissement_fiscal) REFERENCES mode_amortissement (id_mode_amortissement),
  FOREIGN KEY (id_type_immobilisation) REFERENCES type_immobilisation (id_type_immobilisation),

  INDEX idx_date_acquisition (date_acquisition),
  INDEX idx_type (id_type_immobilisation)
);

-- Table log_interaction
CREATE TABLE log_interaction (
  id_log INT PRIMARY KEY NOT NULL AUTO_INCREMENT,

  id_utilisateur INT NOT NULL,
  id_representant INT NULL,

  id_type_action INT NOT NULL,
  description TEXT NOT NULL,

  id_dossier INT NULL,
  id_ecriture_comptable INT NULL,
  id_document INT NULL,
  id_immobilisation INT NULL,
  id_financement INT NULL,
  id_compte INT NULL,
  id_journal INT NULL,

  date_action DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  duree_secondes INT NULL,
  adresse_ip VARCHAR(45) NULL,
  user_agent VARCHAR(255) NULL,

  ancienne_valeur JSON NULL,
  nouvelle_valeur JSON NULL,

  contexte VARCHAR(100) NULL,
  commentaire_utilisateur TEXT NULL,

  FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id_utilisateur) ON DELETE RESTRICT,
  FOREIGN KEY (id_representant) REFERENCES representant (id_representant) ON DELETE SET NULL,
  FOREIGN KEY (id_type_action) REFERENCES type_action (id_type_action) ON DELETE RESTRICT,
  FOREIGN KEY (id_dossier) REFERENCES dossier (id_dossier) ON DELETE CASCADE,
  FOREIGN KEY (id_ecriture_comptable) REFERENCES ecriture_comptable (id_ecriture_comptable) ON DELETE CASCADE,
  FOREIGN KEY (id_document) REFERENCES document (id_document) ON DELETE CASCADE,
  FOREIGN KEY (id_immobilisation) REFERENCES immobilisation (id_immobilisation) ON DELETE CASCADE,
  FOREIGN KEY (id_financement) REFERENCES financement (id_financement) ON DELETE CASCADE,
  FOREIGN KEY (id_compte) REFERENCES compte (id_compte) ON DELETE CASCADE,
  FOREIGN KEY (id_journal) REFERENCES journal (id_journal) ON DELETE CASCADE,

  INDEX idx_utilisateur (id_utilisateur),
  INDEX idx_representant (id_representant),
  INDEX idx_type_action (id_type_action),
  INDEX idx_dossier (id_dossier),
  INDEX idx_date_action (date_action),
  INDEX idx_contexte (contexte),
  INDEX idx_composite_user_date (id_utilisateur, date_action),
  INDEX idx_composite_dossier_date (id_dossier, date_action)
);

-- Table de liaison document - écriture comptable
CREATE TABLE document_ecriture (
  id_document INT NOT NULL,
  id_ecriture_comptable INT NOT NULL,

  PRIMARY KEY (id_document, id_ecriture_comptable),
  FOREIGN KEY (id_document) REFERENCES document (id_document) ON DELETE CASCADE,
  FOREIGN KEY (id_ecriture_comptable) REFERENCES ecriture_comptable (id_ecriture_comptable) ON DELETE CASCADE,

  INDEX idx_ecriture (id_ecriture_comptable)
);

-- Table de liaison document - immobilisation
CREATE TABLE document_immobilisation (
  id_document INT NOT NULL,
  id_immobilisation INT NOT NULL,

  PRIMARY KEY (id_document, id_immobilisation),
  FOREIGN KEY (id_document) REFERENCES document (id_document) ON DELETE CASCADE,
  FOREIGN KEY (id_immobilisation) REFERENCES immobilisation (id_immobilisation) ON DELETE CASCADE,

  INDEX idx_immobilisation (id_immobilisation)
);

-- Table de liaison document - financement
CREATE TABLE document_financement (
  id_document INT NOT NULL,
  id_financement INT NOT NULL,

  PRIMARY KEY (id_document, id_financement),
  FOREIGN KEY (id_document) REFERENCES document (id_document) ON DELETE CASCADE,
  FOREIGN KEY (id_financement) REFERENCES financement (id_financement) ON DELETE CASCADE,

  INDEX idx_financement (id_financement)
);

-- Table de liaison document - représentant
CREATE TABLE document_representant (
  id_document INT NOT NULL,
  id_representant INT NOT NULL,

  PRIMARY KEY (id_document, id_representant),
  FOREIGN KEY (id_document) REFERENCES document (id_document) ON DELETE CASCADE,
  FOREIGN KEY (id_representant) REFERENCES representant (id_representant) ON DELETE CASCADE,

  INDEX idx_representant (id_representant)
);

-- Table de liaison communication-document
CREATE TABLE communication_document (
  id_communication INT NOT NULL,
  id_document INT NOT NULL,

  PRIMARY KEY (id_communication, id_document),
  FOREIGN KEY (id_communication) REFERENCES communication (id_communication) ON DELETE CASCADE,
  FOREIGN KEY (id_document) REFERENCES document (id_document) ON DELETE CASCADE,

  INDEX idx_document (id_document)
);

-- Historique des actions sur les documents
CREATE TABLE historique_document (
  id_historique_document INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  id_document INT NOT NULL,
  id_utilisateur INT NOT NULL,
  action VARCHAR(50) NOT NULL,
  date_action DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  details TEXT NULL,
  adresse_ip VARCHAR(45) NULL,

  FOREIGN KEY (id_document) REFERENCES document (id_document) ON DELETE CASCADE,
  FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id_utilisateur) ON DELETE RESTRICT,

  INDEX idx_document (id_document),
  INDEX idx_utilisateur (id_utilisateur),
  INDEX idx_date_action (date_action),
  INDEX idx_action (action)
);

-- Table des droits d'accès aux documents
CREATE TABLE droit_acces_document (
  id_droit_acces INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  id_document INT NOT NULL,
  id_utilisateur INT NOT NULL,
  lecture BOOLEAN NOT NULL DEFAULT TRUE,
  modification BOOLEAN NOT NULL DEFAULT FALSE,
  suppression BOOLEAN NOT NULL DEFAULT FALSE,
  date_attribution DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_expiration DATETIME NULL,

  UNIQUE KEY uk_document_utilisateur (id_document, id_utilisateur),
  FOREIGN KEY (id_document) REFERENCES document (id_document) ON DELETE CASCADE,
  FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id_utilisateur) ON DELETE CASCADE,

  INDEX idx_document (id_document),
  INDEX idx_utilisateur (id_utilisateur),
  INDEX idx_expiration (date_expiration)
);

-- Table des commentaires sur les documents
CREATE TABLE commentaire_document (
  id_commentaire INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  id_document INT NOT NULL,
  id_utilisateur INT NOT NULL,
  commentaire TEXT NOT NULL,
  date_commentaire DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  id_commentaire_parent INT NULL,

  FOREIGN KEY (id_document) REFERENCES document (id_document) ON DELETE CASCADE,
  FOREIGN KEY (id_utilisateur) REFERENCES utilisateur (id_utilisateur) ON DELETE RESTRICT,
  FOREIGN KEY (id_commentaire_parent) REFERENCES commentaire_document (id_commentaire) ON DELETE CASCADE,

  INDEX idx_document (id_document),
  INDEX idx_utilisateur (id_utilisateur),
  INDEX idx_date (date_commentaire),
  INDEX idx_parent (id_commentaire_parent)
);

-- Table des notifications
CREATE TABLE notification (
  id_notification INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  id_utilisateur_destinataire INT NOT NULL,
  id_utilisateur_emetteur INT NULL,

  titre VARCHAR(200) NOT NULL,
  message TEXT NOT NULL,
  type_notification VARCHAR(45) NOT NULL,

  id_log_interaction INT NULL,
  id_communication INT NULL,
  id_dossier INT NULL,

  date_creation DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_lecture DATETIME NULL,
  lue BOOLEAN NOT NULL DEFAULT FALSE,

  priorite ENUM('BASSE', 'NORMALE', 'HAUTE', 'URGENTE') NOT NULL DEFAULT 'NORMALE',

  FOREIGN KEY (id_utilisateur_destinataire) REFERENCES utilisateur (id_utilisateur) ON DELETE CASCADE,
  FOREIGN KEY (id_utilisateur_emetteur) REFERENCES utilisateur (id_utilisateur) ON DELETE SET NULL,
  FOREIGN KEY (id_log_interaction) REFERENCES log_interaction (id_log) ON DELETE CASCADE,
  FOREIGN KEY (id_communication) REFERENCES communication (id_communication) ON DELETE CASCADE,
  FOREIGN KEY (id_dossier) REFERENCES dossier (id_dossier) ON DELETE CASCADE,

  INDEX idx_destinataire (id_utilisateur_destinataire),
  INDEX idx_date_creation (date_creation),
  INDEX idx_lue (lue),
  INDEX idx_priorite (priorite)
);

-- -----------------------------------------------------
-- Données de référence
-- -----------------------------------------------------

-- Types de journaux
INSERT INTO type_journal (libelle, code) VALUES
('Achats', 'ACH'),
('Ventes', 'VTE'),
('Banque', 'BQ'),
('Caisse', 'CA'),
('Opérations diverses', 'OD'),
('À nouveaux', 'AN');

-- Formes juridiques
INSERT INTO forme_juridique (libelle, code) VALUES
('SARL', 'SARL'),
('SAS', 'SAS'),
('SASU', 'SASU'),
('SA', 'SA'),
('SNC', 'SNC'),
('EI', 'EI'),
('EURL', 'EURL'),
('Auto-entrepreneur', 'AE');

-- Régimes fiscaux
INSERT INTO regime_fiscal (libelle, code) VALUES
('Impôt sur les sociétés', 'IS'),
('Impôt sur le revenu', 'IR'),
('Micro-entreprise', 'MICRO');

-- Régimes TVA
INSERT INTO regime_tva (libelle, code) VALUES
('Réel normal', 'RN'),
('Réel simplifié', 'RS'),
('Franchise en base', 'FB'),
('Mini-réel', 'MR');

-- Modes de remboursement
INSERT INTO mode_remboursement (libelle, code) VALUES
('Amortissement constant', 'AC'),
('Mensualités constantes', 'MC'),
('In fine', 'IF');

-- Modes d'amortissement
INSERT INTO mode_amortissement (libelle, code) VALUES
('Linéaire', 'LIN'),
('Dégressif', 'DEG'),
('Exceptionnel', 'EXC');

-- Types d'immobilisation
INSERT INTO type_immobilisation (libelle, code) VALUES
('Incorporelle', 'INCORP'),
('Corporelle', 'CORP'),
('Financière', 'FIN');

-- Types de compte auxiliaire
INSERT INTO type_compte_auxiliaire (libelle, code) VALUES
('Client', 'CLI'),
('Fournisseur', 'FRS'),
('Autre', 'AUT');

-- Devises
INSERT INTO type_devise (code, libelle, symbole) VALUES
('EUR', 'Euro', '€'),
('USD', 'Dollar américain', '$'),
('GBP', 'Livre sterling', '£'),
('CHF', 'Franc suisse', 'CHF');

-- Rôles utilisateurs
INSERT INTO role_utilisateur (libelle, code, description, niveau_acces) VALUES
('Administrateur', 'ADMIN', 'Accès complet au système', 10),
('Expert-comptable', 'EXPERT', 'Expert-comptable avec accès étendu', 8),
('Comptable confirmé', 'COMPTA_CONF', 'Comptable avec accès complet aux dossiers attribués', 6),
('Comptable junior', 'COMPTA_JR', 'Comptable avec accès limité', 4),
('Assistant comptable', 'ASSISTANT', 'Saisie et consultation uniquement', 3),
('Stagiaire', 'STAGIAIRE', 'Accès en lecture seule', 2),
('Client', 'CLIENT', 'Accès client via portail', 1);

-- Types d'actions pour le logging
INSERT INTO type_action (libelle, code, categorie, niveau_importance) VALUES
-- Dossiers
('Création de dossier', 'DOSSIER_CREATE', 'DOSSIER', 'INFO'),
('Modification de dossier', 'DOSSIER_UPDATE', 'DOSSIER', 'INFO'),
('Suppression de dossier', 'DOSSIER_DELETE', 'DOSSIER', 'CRITICAL'),
('Attribution de dossier', 'DOSSIER_ASSIGN', 'DOSSIER', 'INFO'),
('Consultation de dossier', 'DOSSIER_VIEW', 'DOSSIER', 'INFO'),

-- Écritures comptables
('Création d''écriture', 'ECRITURE_CREATE', 'COMPTABILITE', 'INFO'),
('Modification d''écriture', 'ECRITURE_UPDATE', 'COMPTABILITE', 'WARNING'),
('Suppression d''écriture', 'ECRITURE_DELETE', 'COMPTABILITE', 'CRITICAL'),
('Validation d''écriture', 'ECRITURE_VALIDATE', 'COMPTABILITE', 'INFO'),
('Lettrage d''écriture', 'ECRITURE_LETTRAGE', 'COMPTABILITE', 'INFO'),
('Délettrage d''écriture', 'ECRITURE_UNLETTRAGE', 'COMPTABILITE', 'WARNING'),

-- Comptes
('Création de compte', 'COMPTE_CREATE', 'COMPTABILITE', 'INFO'),
('Modification de compte', 'COMPTE_UPDATE', 'COMPTABILITE', 'INFO'),
('Suppression de compte', 'COMPTE_DELETE', 'COMPTABILITE', 'WARNING'),

-- Journaux
('Création de journal', 'JOURNAL_CREATE', 'COMPTABILITE', 'INFO'),
('Modification de journal', 'JOURNAL_UPDATE', 'COMPTABILITE', 'INFO'),
('Clôture de journal', 'JOURNAL_CLOSE', 'COMPTABILITE', 'WARNING'),

-- Documents
('Téléchargement de document', 'DOC_UPLOAD', 'DOCUMENTS', 'INFO'),
('Consultation de document', 'DOC_VIEW', 'DOCUMENTS', 'INFO'),
('Modification de document', 'DOC_UPDATE', 'DOCUMENTS', 'INFO'),
('Suppression de document', 'DOC_DELETE', 'DOCUMENTS', 'WARNING'),
('Validation de document', 'DOC_VALIDATE', 'DOCUMENTS', 'INFO'),

-- Immobilisations
('Création d''immobilisation', 'IMMO_CREATE', 'IMMOBILISATION', 'INFO'),
('Modification d''immobilisation', 'IMMO_UPDATE', 'IMMOBILISATION', 'INFO'),
('Cession d''immobilisation', 'IMMO_CESSION', 'IMMOBILISATION', 'WARNING'),
('Calcul d''amortissement', 'IMMO_AMORT_CALC', 'IMMOBILISATION', 'INFO'),

-- Financements
('Création de financement', 'FIN_CREATE', 'FINANCEMENT', 'INFO'),
('Modification de financement', 'FIN_UPDATE', 'FINANCEMENT', 'INFO'),
('Remboursement enregistré', 'FIN_REMB', 'FINANCEMENT', 'INFO'),

-- Représentants
('Création de représentant', 'REP_CREATE', 'REPRESENTANT', 'INFO'),
('Modification de représentant', 'REP_UPDATE', 'REPRESENTANT', 'INFO'),
('Liaison dossier-représentant', 'REP_LINK', 'REPRESENTANT', 'INFO'),

-- Système
('Connexion', 'SYS_LOGIN', 'SYSTEME', 'INFO'),
('Déconnexion', 'SYS_LOGOUT', 'SYSTEME', 'INFO'),
('Export de données', 'SYS_EXPORT', 'SYSTEME', 'WARNING'),
('Import de données', 'SYS_IMPORT', 'SYSTEME', 'WARNING'),
('Génération de rapport', 'SYS_REPORT', 'SYSTEME', 'INFO'),
('Sauvegarde', 'SYS_BACKUP', 'SYSTEME', 'CRITICAL');

-- Catégories de documents
INSERT INTO categorie_document (libelle, code, description) VALUES
('Documents comptables', 'COMPTA', 'Documents liés à la comptabilité'),
('Documents juridiques', 'JURID', 'Statuts, contrats, actes juridiques'),
('Documents fiscaux', 'FISCAL', 'Déclarations fiscales et liasses'),
('Documents sociaux', 'SOCIAL', 'Documents RH et sociaux'),
('Documents bancaires', 'BANQUE', 'Relevés, conventions bancaires'),
('Documents commerciaux', 'COMMERCIAL', 'Devis, factures, bons de commande'),
('Documents administratifs', 'ADMIN', 'Courriers administratifs divers');

-- Types de documents
INSERT INTO type_document (libelle, code, id_categorie_document, conservation_annees, obligatoire) VALUES
-- Documents comptables
('Facture d''achat', 'FACT_ACH', 1, 10, TRUE),
('Facture de vente', 'FACT_VTE', 1, 10, TRUE),
('Livre journal', 'LIVRE_J', 1, 10, TRUE),
('Grand livre', 'GRAND_L', 1, 10, TRUE),
('Balance comptable', 'BALANCE', 1, 10, TRUE),
('Pièce justificative', 'PJ', 1, 10, TRUE),

-- Documents juridiques
('Statuts', 'STATUTS', 2, 99, TRUE),
('K-bis', 'KBIS', 2, 99, TRUE),
('Contrat', 'CONTRAT', 2, 10, FALSE),
('PV d''assemblée', 'PV_AG', 2, 10, TRUE),

-- Documents fiscaux
('Déclaration TVA', 'TVA', 3, 10, TRUE),
('Liasse fiscale', 'LIASSE', 3, 10, TRUE),
('Avis d''imposition', 'AVIS_IMP', 3, 10, TRUE),

-- Documents sociaux
('Contrat de travail', 'CT', 4, 99, TRUE),
('Bulletin de paie', 'PAIE', 4, 5, TRUE),
('DSN', 'DSN', 4, 10, TRUE),

-- Documents bancaires
('Relevé bancaire', 'RIB', 5, 10, TRUE),
('Convention de compte', 'CONV_CPT', 5, 10, FALSE),
('Contrat de prêt', 'PRET', 5, 99, TRUE),

-- Documents commerciaux
('Devis', 'DEVIS', 6, 5, FALSE),
('Bon de commande', 'BC', 6, 10, FALSE),
('Bon de livraison', 'BL', 6, 10, FALSE);

-- Statuts de documents
INSERT INTO statut_document (libelle, code, description) VALUES
('Brouillon', 'BROUILLON', 'Document en cours de préparation'),
('En attente de validation', 'ATTENTE_VAL', 'Document en attente de validation'),
('Validé', 'VALIDE', 'Document validé et finalisé'),
('Archivé', 'ARCHIVE', 'Document archivé'),
('Obsolète', 'OBSOLETE', 'Document remplacé ou périmé'),
('Supprimé', 'SUPPRIME', 'Document marqué comme supprimé');