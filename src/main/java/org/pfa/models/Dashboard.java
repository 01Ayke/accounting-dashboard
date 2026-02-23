package org.pfa.models;

public class Dashboard {
    private File file;
    private int[] turnover =  {0,0,0};
    private Double resultat;

    public Dashboard(File file) {
        this.file = file;

        // resultat = somme de classe 7 - somme de classe 6
        this.resultat = file.getComptes()
                .stream()
                .mapToDouble(c -> {
                    if (c.getActif()  && c.getNumero_compte().startsWith("7")) {
                        return c.getSolde();
                    }
                    else if (c.getActif() && c.getNumero_compte().startsWith("6")) {
                        return -c.getSolde();
                    }
                    else return 0.0;
                }
                ).sum();
    }

    public File getFile() {
        return this.file;
    }

    public int[] getTurnover() {
        return this.turnover;
    }

    public Double getResultat() { return this.resultat; }
}
