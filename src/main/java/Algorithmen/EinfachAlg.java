package Algorithmen;

import model.DataStructure;

/**
 * Algorithmus zur Berechnung der einfachen Fahrt ohne Wartezeiten.
 * Markiert lediglich Kollisionen, ändert aber nicht den Fahrplan.
 */
public class EinfachAlg implements Algorithmus {


    @Override
    public DataStructure algorithmus(DataStructure dto) {

        int anzahl = dto.getAnzahl();
        int[] hinweg = dto.getHinweg();
        int[] rueckweg = dto.getRueckweg();

        String[] kollisionen = new String[anzahl - 1];

        if (hatKollision(dto)) {
            for (int i = 0; i < anzahl-1; i++) {

                int startHin = hinweg[i * 2];
                int endeHin = hinweg[i * 2 + 1];

                int startRueck = rueckweg[i * 2+1];

                int normiertesEndeHin;

                if (endeHin < startHin) {
                    normiertesEndeHin = endeHin + 60;
                } else {
                    normiertesEndeHin = endeHin;
                }
                if(startRueck <= normiertesEndeHin && startRueck >= startHin) {
                    kollisionen[i] = "  x";
                }
            }
        }
        dto.setKollisionen(kollisionen);

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