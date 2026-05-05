package Algorithmen;

import model.DataStructure;

/**
 * Schnittstelle für alle Algorithmen, die Fahrplankollisionen erkennen und/oder auflösen.
 */
public interface Algorithmus {

    /**
     * Führt den jeweiligen Algorithmus auf den Daten aus, um den Fahrplan anzupassen.
     *
     * @param dto Die Eingabedatenstruktur.
     * @return Die angepasste Datenstruktur mit den neuen Fahrzeiten.
     */
    DataStructure algorithmus(DataStructure dto);

    /**
     * Prüft, ob es innerhalb des Fahrplans Kollisionen auf der Strecke gibt.
     *
     * @param dto Die zu prüfende Datenstruktur.
     * @return True, wenn eine Kollision vorliegt, andernfalls false.
     */
    boolean hatKollision(DataStructure dto);

    /**
     * Überprüft, ob sich zwei Fahrten zeitlich überschneiden.
     *
     * @param startHin   Startzeit der Hinfahrt.
     * @param endeHin    Endzeit der Hinfahrt.
     * @param startRueck Startzeit der Rückfahrt.
     * @param endeRueck  Endzeit der Rückfahrt.
     * @return True, wenn es eine zeitliche Überschneidung gibt.
     */
    boolean istZeitueberlappend(int startHin, int endeHin, int startRueck, int  endeRueck);
}
