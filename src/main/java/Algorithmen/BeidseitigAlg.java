package Algorithmen;

import model.DataStructure;

/**
 * Algorithmus zur Lösung von Streckenkollisionen durch beidseitiges Warten.
 * Die Wartezeiten werden möglichst gleichmäßig auf Hinfahrt und Rückfahrt aufgeteilt.
 */
public class BeidseitigAlg implements Algorithmus {

    @Override
    public DataStructure algorithmus(DataStructure dto) {

        if (!hatKollision(dto)) {
            return dto;
        }

        int anzahl = dto.getAnzahl();

        // 1. Einseitige Lösung auf einer Kopie simulieren, um Konfliktpunkte zu finden
        DataStructure copyDto = dto.deepCopy();
        EinseitigAlg einseitig = new EinseitigAlg();
        copyDto = einseitig.algorithmus(copyDto);

        // Das sind die optimalen Orte und Zeiten, wenn nur einer warten würde
        int[] einseitigWarte = copyDto.getWartezeitRueck();

        // 2. Wartezeiten 50/50 auf Hin- und Rückweg aufteilen
        int[] warteHin = new int[anzahl];
        int[] warteRueck = new int[anzahl];
        int gesamtWartezeitHin = 0;
        int gesamtWartezeitRueck = 0;

        for (int i = 0; i < anzahl; i++) {
            int w = einseitigWarte[i];
            warteHin[i] = w / 2;
            warteRueck[i] = w - warteHin[i]; // Rest bei ungeraden Zahlen geht an den Rückweg

            gesamtWartezeitHin += warteHin[i];
            gesamtWartezeitRueck += warteRueck[i];
        }

        // 3. Hinweg mit den neuen Wartezeiten neu berechnen
        int[] hinweg = dto.getHinweg().clone();
        int zeitHin = hinweg[0]; // Startzeit am Bahnhof A (Index 0 = Abfahrt)

        for (int i = 0; i < anzahl - 1; i++) {
            // Fahrzeit zum nächsten Bahnhof addieren
            zeitHin = (zeitHin + dto.getAbstaende()[i]) % 60;
            hinweg[i * 2 + 1] = zeitHin; // Ankunft ist auf ungeraden Indizes (1, 3, 5...)

            // Wenn es nicht der Endbahnhof (J) ist, kommt Halt + Wartezeit dazu
            if (i + 1 < anzahl - 1) {
                zeitHin = (zeitHin + 1 + warteHin[i + 1]) % 60;
                hinweg[(i + 1) * 2] = zeitHin; // Abfahrt ist auf geraden Indizes (2, 4, 6...)
            }
        }
        dto.setHinweg(hinweg);
        dto.setWartezeitHin(warteHin);

        // 4. Rückweg mit den neuen Wartezeiten neu berechnen
        int[] rueckweg = dto.getRueckweg().clone();

        // Neue Startzeit des Rückwegs am Endbahnhof J berechnen
        // Letzte Ankunft Hinweg liegt immer auf dem allerletzten Index (anzahl-2) * 2 + 1
        int letzteAnkunftHin = hinweg[(anzahl - 2) * 2 + 1];
        int zeitRueck = (letzteAnkunftHin + 1) % 60;

        for (int i = anzahl - 2; i >= 0; i--) {
            // Abfahrt am Startbahnhof dieser Teilstrecke
            // Wenn es NICHT der erste Startbahnhof der Rückreise (J) ist
            if (i < anzahl - 2) {
                zeitRueck = (zeitRueck + 1 + warteRueck[i + 1]) % 60;
            }
            rueckweg[i * 2 + 1] = zeitRueck; // Abfahrt Richtung Heimat

            // Fahrzeit zum nächsten Bahnhof addieren (Richtung A)
            zeitRueck = (zeitRueck + dto.getAbstaende()[i]) % 60;
            rueckweg[i * 2] = zeitRueck; // Ankunft dort
        }
        dto.setRueckweg(rueckweg);
        dto.setWartezeitRueck(warteRueck);

        // 5. Metadaten und Strafen aktualisieren
        dto.setGesamtdauerHin(dto.getMinDauer() + gesamtWartezeitHin);
        dto.setGesamtdauerRueck(dto.getMinDauer() + gesamtWartezeitRueck);
        dto.setStrafen(gesamtWartezeitHin * gesamtWartezeitHin + gesamtWartezeitRueck * gesamtWartezeitRueck);

        return dto;
    }

    @Override
    public boolean hatKollision(DataStructure dto) {

        for (int i = 0; i < dto.getAnzahl() - 1; i++) {

            int startHin = dto.getHinweg()[i * 2];
            int endeHin = dto.getHinweg()[i * 2 + 1];

            int sicherheitHin = (endeHin + 1) % 60;

            int startRueck = dto.getRueckweg()[i * 2 + 1];
            int endeRueck = dto.getRueckweg()[i * 2];

            if (istZeitueberlappend(startHin, sicherheitHin, startRueck, endeRueck)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean istZeitueberlappend(int startHin, int endeHin, int startRueck, int endeRueck) {
        // Diese Logik vergleicht die Block-Längen auf einem runden Ziffernblatt.
        // Sie ist immun gegen den 60-Minuten-Sprung (z.B. von 55 auf 05 Uhr).
        int lenHin = (endeHin - startHin + 60) % 60;
        int lenRueck = (endeRueck - startRueck + 60) % 60;

        int distHinToRueck = (startRueck - startHin + 60) % 60;
        int distRueckToHin = (startHin - startRueck + 60) % 60;

        return distHinToRueck < lenHin || distRueckToHin < lenRueck;
    }
}