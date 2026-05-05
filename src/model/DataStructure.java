package model;

public class DataStructure {

    private String[] strecke;
    private int[] abstaende;
    private int start;
    private int anzahl;
    private int minDauer;
    private int[] hinweg;
    private int[] rueckweg;

    private String[] kollisionen;
    private int[] wartezeitHin;
    private int[] wartezeitRueck;
    private int strafen;

    private int gesamtdauerHin;
    private int gesamtdauerRueck;

    public DataStructure deepCopy() {
        DataStructure copy = new DataStructure();

        copy.setStart(this.start);
        copy.setAnzahl(this.anzahl);
        copy.setMinDauer(this.minDauer);
        copy.setStrafen(this.strafen);
        copy.setGesamtdauerHin(this.gesamtdauerHin);
        copy.setGesamtdauerRueck(this.gesamtdauerRueck);
        copy.setWartezeitHin(this.wartezeitHin); //ToDo
        copy.setWartezeitRueck(this.wartezeitRueck); //ToDo


        if (this.strecke != null) copy.setStrecke(this.strecke.clone());
        if (this.abstaende != null) copy.setAbstaende(this.abstaende.clone());
        if (this.hinweg != null) copy.setHinweg(this.hinweg.clone());
        if (this.rueckweg != null) copy.setRueckweg(this.rueckweg.clone());

        if (this.kollisionen != null) copy.setKollisionen(this.kollisionen.clone());

        return copy;
    }

    // ==========================================
    //               GETTER & SETTER
    // ==========================================

    public String[] getStrecke() {
        return strecke;
    }

    public void setStrecke(String[] strecke) {
        this.strecke = strecke;
    }

    public int[] getAbstaende() {
        return abstaende;
    }

    public void setAbstaende(int[] abstaende) {
        this.abstaende = abstaende;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    public int getMinDauer() {
        return minDauer;
    }

    public void setMinDauer(int minDauer) {
        this.minDauer = minDauer;
    }

    public int[] getHinweg() {
        return hinweg;
    }

    public void setHinweg(int[] hinweg) {
        this.hinweg = hinweg;
    }

    public int[] getRueckweg() {
        return rueckweg;
    }

    public void setRueckweg(int[] rueckweg) {
        this.rueckweg = rueckweg;
    }

    public String[] getKollisionen() {
        return kollisionen;
    }

    public void setKollisionen(String[] kollisionen) {
        this.kollisionen = kollisionen;
    }

    public int[] getWartezeitHin() {
        return wartezeitHin;
    }

    public void setWartezeitHin(int[] wartezeitHin) {
        this.wartezeitHin = wartezeitHin;
    }

    public int[] getWartezeitRueck() {
        return wartezeitRueck;
    }

    public void setWartezeitRueck(int[] wartezeitRueck) {
        this.wartezeitRueck = wartezeitRueck;
    }

    public int getStrafen() {
        return strafen;
    }

    public void setStrafen(int strafen) {
        this.strafen = strafen;
    }

    public int getGesamtdauerHin() {
        return gesamtdauerHin;
    }

    public void setGesamtdauerHin(int gesamtdauerHin) {
        this.gesamtdauerHin = gesamtdauerHin;
    }

    public int getGesamtdauerRueck() {
        return gesamtdauerRueck;
    }

    public void setGesamtdauerRueck(int gesamtdauerRueck) {
        this.gesamtdauerRueck = gesamtdauerRueck;
    }
}