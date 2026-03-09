package org.pfa.models;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SIGTest {

    @Test
    void getMargeCommerciale() {
        Set<Compte> comptes = new HashSet<>();
        comptes.add(new Compte(new File(), "707", "libelle", 20.0F, true));
        comptes.add(new Compte(new File(), "607", "libelle", 10.0F, true));
        comptes.add(new Compte(new File(), "6071", "libelle", 5.0F, true));
        SIG sig = new SIG(comptes);
        assert(sig.getMargeCommerciale() == 5.0);
    }

    @Test
    void getProductionExercice() {
        Set<Compte> comptes = new HashSet<>();
        comptes.add(new Compte(new File(), "701", "libelle", 20.0F, true));
        comptes.add(new Compte(new File(), "713", "libelle", 10.0F, true));
        comptes.add(new Compte(new File(), "72", "libelle", 5.0F, true));
        comptes.add(new Compte(new File(), "707", "libelle", 20.0F, true));
        SIG sig = new SIG(comptes);
        assert(sig.getProductionExercice() == 35.0);
    }

    @Test
    void getValeurAjoutee() {
        Set<Compte> comptes = new HashSet<>();
        comptes.add(new Compte(new File(), "707", "libelle", 20.0F, true));
        comptes.add(new Compte(new File(), "607", "libelle", 10.0F, true));
        comptes.add(new Compte(new File(), "6071", "libelle", 5.0F, true));
        comptes.add(new Compte(new File(), "701", "libelle", 20.0F, true));
        comptes.add(new Compte(new File(), "713", "libelle", 10.0F, true));
        comptes.add(new Compte(new File(), "72", "libelle", 5.0F, true));
        comptes.add(new Compte(new File(), "6019", "libelle", 20.0F, true));
        SIG sig = new SIG(comptes);
        assert(sig.getValeurAjoutee() == 20.0);
    }

    @Test
    void getExcedentBrut() {
        Set<Compte> comptes = new HashSet<>();
        comptes.add(new Compte(new File(), "707", "libelle", 20.0F, true));
        comptes.add(new Compte(new File(), "607", "libelle", 10.0F, true));
        comptes.add(new Compte(new File(), "6071", "libelle", 5.0F, true));
        comptes.add(new Compte(new File(), "701", "libelle", 20.0F, true));
        comptes.add(new Compte(new File(), "713", "libelle", 10.0F, true));
        comptes.add(new Compte(new File(), "72", "libelle", 5.0F, true));
        comptes.add(new Compte(new File(), "6019", "libelle", 20.0F, true));
        comptes.add(new Compte(new File(), "63", "libelle", 3.0F, true));
        comptes.add(new Compte(new File(), "641", "libelle", 3.0F, true));
        comptes.add(new Compte(new File(), "642", "libelle", 3.0F, true));
        SIG sig = new SIG(comptes);
        assert(sig.getExcedentBrut() == 14.0);
    }

    @Test
    void getResultatExploitation() {
        Set<Compte> comptes = new HashSet<>();
        comptes.add(new Compte(new File(), "707", "libelle", 20.0F, true));
        comptes.add(new Compte(new File(), "607", "libelle", 10.0F, true));
        comptes.add(new Compte(new File(), "6071", "libelle", 5.0F, true));
        comptes.add(new Compte(new File(), "701", "libelle", 20.0F, true));
        comptes.add(new Compte(new File(), "713", "libelle", 10.0F, true));
        comptes.add(new Compte(new File(), "72", "libelle", 5.0F, true));
        comptes.add(new Compte(new File(), "6019", "libelle", 20.0F, true));
        comptes.add(new Compte(new File(), "63", "libelle", 3.0F, true));
        comptes.add(new Compte(new File(), "641", "libelle", 3.0F, true));
        comptes.add(new Compte(new File(), "642", "libelle", 3.0F, true));
        comptes.add(new Compte(new File(), "781", "libelle", 4.0F, true));
        comptes.add(new Compte(new File(), "6815", "libelle", 7.0F, true));
        SIG sig = new SIG(comptes);
        assert(sig.getResultatExploitation() == 11.0);
    }

    @Test
    void getResultatFinancier() {
        Set<Compte> comptes = new HashSet<>();
        comptes.add(new Compte(new File(), "76", "libelle", 6.0F, true));
        comptes.add(new Compte(new File(), "661", "libelle", 4.0F, true));
        comptes.add(new Compte(new File(), "771", "libelle", 6.0F, true));
        SIG sig = new SIG(comptes);
        assert(sig.getResultatFinancier() == 2.0);
    }

    @Test
    void getResultatExceptionnel() {
        Set<Compte> comptes = new HashSet<>();
        comptes.add(new Compte(new File(), "76", "libelle", 6.0F, true));
        comptes.add(new Compte(new File(), "661", "libelle", 4.0F, true));
        comptes.add(new Compte(new File(), "771", "libelle", 6.0F, true));
        comptes.add(new Compte(new File(), "673", "libelle", 5.0F, true));
        SIG sig = new SIG(comptes);
        assert(sig.getResultatExceptionnel() == 1.0);
    }

    @Test
    void getResultatExercice() {
        Set<Compte> comptes = new HashSet<>();
        comptes.add(new Compte(new File(), "707", "libelle", 20.0F, true));
        comptes.add(new Compte(new File(), "607", "libelle", 10.0F, true));
        comptes.add(new Compte(new File(), "6071", "libelle", 5.0F, true));
        comptes.add(new Compte(new File(), "701", "libelle", 20.0F, true));
        comptes.add(new Compte(new File(), "713", "libelle", 10.0F, true));
        comptes.add(new Compte(new File(), "72", "libelle", 5.0F, true));
        comptes.add(new Compte(new File(), "6019", "libelle", 20.0F, true));
        comptes.add(new Compte(new File(), "63", "libelle", 3.0F, true));
        comptes.add(new Compte(new File(), "641", "libelle", 3.0F, true));
        comptes.add(new Compte(new File(), "642", "libelle", 3.0F, true));
        comptes.add(new Compte(new File(), "781", "libelle", 4.0F, true));
        comptes.add(new Compte(new File(), "6815", "libelle", 7.0F, true));
        comptes.add(new Compte(new File(), "76", "libelle", 6.0F, true));
        comptes.add(new Compte(new File(), "661", "libelle", 4.0F, true));
        comptes.add(new Compte(new File(), "771", "libelle", 6.0F, true));
        SIG sig = new SIG(comptes);
        assert(sig.getResultatExercice() == 7.0);
    }
}