package model;

/**
 * Datenstruktur zur Speicherung aller relevanten Informationen einer Zugstrecke. Diese Klasse enthält sowohl die
 * Eingabedaten (Bahnhöfe, Abstände, Startzeit) als auch die berechneten Fahrpläne und Metriken (Wartezeiten,
 * Kollisionen, Strafen).
 *
 * @author Felix Vauth
 * @version 1.0
 */
public class DataStructure {

    /** Namen der Bahnhöfe auf der Strecke in Reihenfolge. */
    private String[] strecke;

    /** Fahrzeiten in Minuten zwischen benachbarten Bahnhöfen. */
    private int[] abstaende;

    /** Startzeit der Hinfahrt in Minuten (0-59). */
    private int start;

    /** Anzahl der Bahnhöfe auf der Strecke. */
    private int anzahl;

    /** Minimale Fahrtdauer ohne Wartezeiten in Minuten. */
    private int minDauer;

    /** Fahrzeiten der Hinfahrt: [Abfahrt0, Ankunft1, Abfahrt1, Ankunft2, ...]. */
    private int[] hinweg;

    /** Fahrzeiten der Rückfahrt: [AnkunftN-1, AbfahrtN-1, AnkunftN-2, ...]. */
    private int[] rueckweg;

    /** Mindestumstiegszeit an Bahnhöfen in Minuten. */
    private int umstiegszeit;

    /** Sicherheitswartezeit die ein Zug wartet, bevor ein Strecke wieder befahren werden darf. */
    private int sicherheitszeit;

    /** Kollisionsmarkierungen pro Streckenabschnitt ("x" bei Kollision). */
    private String[] kollisionen;

    /** Wartezeiten an jedem Bahnhof während der Hinfahrt in Minuten. */
    private int[] wartezeitHin;

    /** Wartezeiten an jedem Bahnhof während der Rückfahrt in Minuten. */
    private int[] wartezeitRueck;

    /** Summe der Strafpunkte (quadrierte Wartezeiten). */
    private int strafen;

    /** Gesamtdauer der Hinfahrt inkl. Wartezeiten in Minuten. */
    private int gesamtdauerHin;

    /** Gesamtdauer der Rückfahrt inkl. Wartezeiten in Minuten. */
    private int gesamtdauerRueck;

    /**
     * Erstellt eine tiefe Kopie dieser Datenstruktur. Alle Arrays werden geklont, sodass Änderungen an der Kopie keine
     * Auswirkungen auf das Original haben.
     *
     * @return eine unabhängige Kopie dieser Datenstruktur
     */
    public DataStructure deepCopy() {
        DataStructure copy = new DataStructure();

        copy.setStart(this.start);
        copy.setAnzahl(this.anzahl);
        copy.setMinDauer(this.minDauer);
        copy.setStrafen(this.strafen);
        copy.setGesamtdauerHin(this.gesamtdauerHin);
        copy.setGesamtdauerRueck(this.gesamtdauerRueck);
        copy.setUmstiegszeit(this.umstiegszeit);
        copy.setSicherheitszeit(this.sicherheitszeit);

        if (this.wartezeitRueck != null) {
            copy.setWartezeitRueck(this.wartezeitRueck.clone());
        }
        if (this.wartezeitHin != null) {
            copy.setWartezeitHin(this.wartezeitHin.clone());
        }
        if (this.strecke != null) {
            copy.setStrecke(this.strecke.clone());
        }
        if (this.abstaende != null) {
            copy.setAbstaende(this.abstaende.clone());
        }
        if (this.hinweg != null) {
            copy.setHinweg(this.hinweg.clone());
        }
        if (this.rueckweg != null) {
            copy.setRueckweg(this.rueckweg.clone());
        }
        if (this.kollisionen != null) {
            copy.setKollisionen(this.kollisionen.clone());
        }

        return copy;
    }

    /**
     * Gibt die Bahnhofsnamen der Strecke zurück.
     *
     * @return Array mit Bahnhofsnamen
     */
    public String[] getStrecke() {
        return strecke;
    }

    /**
     * Setzt die Bahnhofsnamen der Strecke.
     *
     * @param strecke Array mit Bahnhofsnamen
     */
    public void setStrecke(String[] strecke) {
        this.strecke = strecke;
    }

    /**
     * Gibt die Fahrzeiten zwischen den Bahnhöfen zurück.
     *
     * @return Array mit Fahrzeiten in Minuten
     */
    public int[] getAbstaende() {
        return abstaende;
    }

    /**
     * Setzt die Fahrzeiten zwischen den Bahnhöfen.
     *
     * @param abstaende Array mit Fahrzeiten in Minuten
     */
    public void setAbstaende(int[] abstaende) {
        this.abstaende = abstaende;
    }

    /**
     * Gibt die Startzeit des Hinwegs zurück.
     *
     * @return Startzeit in Minuten seit Mitternacht
     */
    public int getStart() {
        return start;
    }

    /**
     * Setzt die Startzeit des Hinwegs.
     *
     * @param start Startzeit in Minuten seit Mitternacht
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * Gibt die Anzahl der Bahnhöfe zurück.
     *
     * @return Anzahl der Bahnhöfe
     */
    public int getAnzahl() {
        return anzahl;
    }

    /**
     * Setzt die Anzahl der Bahnhöfe.
     *
     * @param anzahl Anzahl der Bahnhöfe
     */
    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    /**
     * Gibt die Mindestdauer zurück.
     *
     * @return Mindestdauer in Minuten
     */
    public int getMinDauer() {
        return minDauer;
    }

    /**
     * Setzt die Mindestdauer.
     *
     * @param minDauer Mindestdauer in Minuten
     */
    public void setMinDauer(int minDauer) {
        this.minDauer = minDauer;
    }

    /**
     * Gibt die Ankunftszeiten auf dem Hinweg zurück.
     *
     * @return Array mit Ankunftszeiten in Minuten seit Mitternacht
     */
    public int[] getHinweg() {
        return hinweg;
    }

    /**
     * Setzt die Ankunftszeiten auf dem Hinweg.
     *
     * @param hinweg Array mit Ankunftszeiten in Minuten seit Mitternacht
     */
    public void setHinweg(int[] hinweg) {
        this.hinweg = hinweg;
    }

    /**
     * Gibt die Ankunftszeiten auf dem Rückweg zurück.
     *
     * @return Array mit Ankunftszeiten in Minuten seit Mitternacht
     */
    public int[] getRueckweg() {
        return rueckweg;
    }

    /**
     * Setzt die Ankunftszeiten auf dem Rückweg.
     *
     * @param rueckweg Array mit Ankunftszeiten in Minuten seit Mitternacht
     */
    public void setRueckweg(int[] rueckweg) {
        this.rueckweg = rueckweg;
    }

    /**
     * Gibt die Umstiegszeit zurück.
     *
     * @return Umstiegszeit in Minuten
     */
    public int getUmstiegszeit() {
        return umstiegszeit;
    }

    /**
     * Setzt die Umstiegszeit.
     *
     * @param umstiegszeit Umstiegszeit in Minuten
     */
    public void setUmstiegszeit(int umstiegszeit) {
        this.umstiegszeit = umstiegszeit;
    }

    /**
     * Gibt die Sicherheitszeit zurück.
     *
     * @return Sicherheitszeit in Minuten
     */
    public int getSicherheitszeit() {
        return sicherheitszeit;
    }

    /**
     * Setzt die Sicherheitszeit.
     *
     * @param sicherheitszeit Sicherheitszeit in Minuten
     */
    public void setSicherheitszeit(int sicherheitszeit) {
        this.sicherheitszeit = sicherheitszeit;
    }

    /**
     * Gibt die Kollisionsmarkierungen zurück.
     *
     * @return Array mit Kollisionsmarkierungen ("X" oder "")
     */
    public String[] getKollisionen() {
        return kollisionen;
    }

    /**
     * Setzt die Kollisionsmarkierungen.
     *
     * @param kollisionen Array mit Kollisionsmarkierungen
     */
    public void setKollisionen(String[] kollisionen) {
        this.kollisionen = kollisionen;
    }

    /**
     * Gibt die Wartezeiten auf dem Hinweg zurück.
     *
     * @return Array mit Wartezeiten in Minuten
     */
    public int[] getWartezeitHin() {
        return wartezeitHin;
    }

    /**
     * Setzt die Wartezeiten auf dem Hinweg.
     *
     * @param wartezeitHin Array mit Wartezeiten in Minuten
     */
    public void setWartezeitHin(int[] wartezeitHin) {
        this.wartezeitHin = wartezeitHin;
    }

    /**
     * Gibt die Wartezeiten auf dem Rückweg zurück.
     *
     * @return Array mit Wartezeiten in Minuten
     */
    public int[] getWartezeitRueck() {
        return wartezeitRueck;
    }

    /**
     * Setzt die Wartezeiten auf dem Rückweg.
     *
     * @param wartezeitRueck Array mit Wartezeiten in Minuten
     */
    public void setWartezeitRueck(int[] wartezeitRueck) {
        this.wartezeitRueck = wartezeitRueck;
    }

    /**
     * Gibt die Anzahl der Strafpunkte zurück.
     *
     * @return Anzahl der Strafpunkte
     */
    public int getStrafen() {
        return strafen;
    }

    /**
     * Setzt die Anzahl der Strafpunkte.
     *
     * @param strafen Anzahl der Strafpunkte
     */
    public void setStrafen(int strafen) {
        this.strafen = strafen;
    }

    /**
     * Gibt die Gesamtdauer des Hinwegs zurück.
     *
     * @return Gesamtdauer in Minuten
     */
    public int getGesamtdauerHin() {
        return gesamtdauerHin;
    }

    /**
     * Setzt die Gesamtdauer des Hinwegs.
     *
     * @param gesamtdauerHin Gesamtdauer in Minuten
     */
    public void setGesamtdauerHin(int gesamtdauerHin) {
        this.gesamtdauerHin = gesamtdauerHin;
    }

    /**
     * Gibt die Gesamtdauer des Rückwegs zurück.
     *
     * @return Gesamtdauer in Minuten
     */
    public int getGesamtdauerRueck() {
        return gesamtdauerRueck;
    }

    /**
     * Setzt die Gesamtdauer des Rückwegs.
     *
     * @param gesamtdauerRueck Gesamtdauer in Minuten
     */
    public void setGesamtdauerRueck(int gesamtdauerRueck) {
        this.gesamtdauerRueck = gesamtdauerRueck;
    }
}
