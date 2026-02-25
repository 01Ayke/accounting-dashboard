package org.pfa.models;

public class Dashboard {
    private File file;
    private int[] turnover =  {0,0,0};
    private SIG SIG;

    public Dashboard(File file) {
        this.file = file;

        this.SIG = new SIG(file.getComptes());
    }

    public File getFile() {
        return this.file;
    }

    public int[] getTurnover() {
        return this.turnover;
    }

    public SIG getSIG() { return this.SIG; }
}
