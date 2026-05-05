package Algorithmen;

import model.DataStructure;

/**
 * Algorithmus zur Lösung von Streckenkollisionen durch einseitiges Warten von Rückfahrten.
 * Rückzüge warten an Bahnhöfen, um Hinzüge durchzulassen.
 */
public class EinseitigAlg implements Algorithmus {


    @Override
    public DataStructure algorithmus(DataStructure dto) {
        int anzahl = dto.getAnzahl();
        int[] warteZeitenRueck = new int[anzahl];
        int[] hinweg = dto.getHinweg();
        int[] rueckweg = dto.getRueckweg();

        while (hatKollision(dto)) {

            String[] kollisionen = new String[anzahl - 1];

            // 1. Exakt dieselbe Kollisionsprüfung nutzen!
            for (int i = 0; i < anzahl - 1; i++) {
                int startHin = hinweg[i * 2];
                int endeHin = hinweg[i * 2 + 1];
                int sicherheitHin = (endeHin + 1) % 60;

                int startRueck = rueckweg[i * 2 + 1];
                int endeRueck = rueckweg[i * 2];

                if (istZeitueberlappend(startHin, sicherheitHin, startRueck, endeRueck)) {
                    kollisionen[i] = "x";
                }
            }

            // 2. Kollision reparieren
            for (int i = kollisionen.length - 1; i >= 0; i--) {
                if (kollisionen[i] != null) {

                    // Sicherheit bedeutet: Der Rückzug darf erst abfahren,
                    // wenn der Hinzug angekommen ist (+1 Min Puffer)
                    int sicherheitHin = (hinweg[i * 2 + 1] + 1) % 60;
                    int startRueck = rueckweg[i * 2 + 1];

                    // Saubere Modulo-Berechnung der benötigten Verschiebung
                    int diff = (sicherheitHin - startRueck + 60) % 60;

                    // Fallback, falls die Startzeit theoretisch schon gleich ist,
                    // aber das Ende noch überlappt
                    if (diff == 0) diff = 1;

                    warteZeitenRueck[i + 1] += diff;

                    // Alle Zeiten des Rückwegs ab hier verschieben
                    for (int j = 2 * i + 1; j >= 0; j--) {
                        rueckweg[j] = (rueckweg[j] + diff) % 60;
                    }
                    dto.setRueckweg(rueckweg);
                    dto.setWartezeitRueck(warteZeitenRueck);
                    break;
                }
            }
        }

        int wartezeitRueckGesamt = 0;
        for (int i = 0; i < warteZeitenRueck.length; i++) {
            wartezeitRueckGesamt += warteZeitenRueck[i];
        }
        dto.setGesamtdauerRueck(dto.getGesamtdauerRueck() + wartezeitRueckGesamt);
        dto.setStrafen(wartezeitRueckGesamt * wartezeitRueckGesamt);

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