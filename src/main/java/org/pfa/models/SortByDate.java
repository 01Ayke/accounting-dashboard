package org.pfa.models;

import java.util.Comparator;
import java.util.Date;

public class SortByDate implements Comparator<LigneGrandLivre> {
    public int compare(LigneGrandLivre ce1, LigneGrandLivre ce2) {
        if (ce1.getDate() == null) return -1;
        if (ce2.getDate() == null) return 1;
        if (ce1.getDate().before(ce2.getDate())) return -1;
        if (ce1.getDate().after(ce2.getDate())) return 1;
        return 0;
    }
}
