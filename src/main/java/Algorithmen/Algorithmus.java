package Algorithmen;

import model.DataStructure;

/**
 * Schnittstelle für Fahrplan-Algorithmen zur Kollisionserkennung und -auflösung.
 * Implementierende Klassen müssen Methoden bereitstellen, um Fahrpläne zu berechnen, Kollisionen zu erkennen und
 * zeitliche Überschneidungen zu prüfen.
 *
 * @see EinfachAlg
 * @see EinseitigAlg
 * @see BeidseitigAlg
 */
public interface Algorithmus {

    /**
     * Führt den Algorithmus auf der übergebenen Datenstruktur aus.
     * Je nach Implementierung werden Kollisionen markiert oder durch Anpassung der Wartezeiten aufgelöst.
     *
     * @param dto die Eingabedatenstruktur mit Strecken- und Fahrplandaten
     * @return die modifizierte Datenstruktur mit angepassten Fahrzeiten und Wartezeiten
     */
    DataStructure algorithmus(DataStructure dto);

    /**
     * Prüft, ob der Fahrplan Kollisionen auf der eingleisigen Strecke enthält.
     *
     * @param dto die zu prüfende Datenstruktur
     * @return {@code true} wenn mindestens eine Kollision vorliegt, sonst {@code false}
     */
    boolean hatKollision(DataStructure dto);

    /**
     * Überprüft, ob sich zwei Streckenabschnitte zeitlich überschneiden.
     * Die Berechnung berücksichtigt den Überlauf bei Modulo-60-Zeitangaben (zirkuläre Zeitdarstellung im
     * Stundenbereich).
     *
     * @param startHin Abfahrtszeit der Hinfahrt in Minuten (0-59)
     * @param endeHin Ankunftszeit der Hinfahrt inkl. Sicherheitszeit in Minuten (0-59)
     * @param startRueck Abfahrtszeit der Rückfahrt in Minuten (0-59)
     * @param endeRueck Ankunftszeit der Rückfahrt in Minuten (0-59)
     * @return {@code true} wenn eine zeitliche Überschneidung besteht
     */
    boolean istZeitueberlappend(int startHin, int endeHin, int startRueck, int endeRueck);
}
