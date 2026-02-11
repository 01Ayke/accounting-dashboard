package org.pfa.models;

public class Dashboard {
    private File file;
    private int[] turnover =  {0,0,0};

    public Dashboard(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public int[] getTurnover() {
        return this.turnover;
    }
}
