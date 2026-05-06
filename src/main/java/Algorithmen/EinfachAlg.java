package Algorithmen;

import model.DataStructure;

/**
 * Algorithmus für die einfache Fahrt ohne Kollisionsauflösung. Dieser Algorithmus markiert lediglich Kollisionsstellen
 * im Fahrplan, nimmt aber keine Anpassungen an den Fahrzeiten vor. Er dient als Referenz für den ursprünglichen,
 * unveränderten Fahrplan.
 *
 * @see Algorithmus
 */
public class EinfachAlg implements Algorithmus {

    /**
     * {@inheritDoc} Markiert Kollisionen mit "x" im Kollisions-Array, ohne den Fahrplan zu ändern.
     */
    @Override
    public DataStructure algorithmus(DataStructure dto) {
        int anzahl = dto.getAnzahl();
        int[] hinweg = dto.getHinweg();
        int[] rueckweg = dto.getRueckweg();
        String[] kollisionen = new String[anzahl - 1];

        if (hatKollision(dto)) {
            for (int i = 0; i < anzahl - 1; i++) {
                int startHin = hinweg[i * 2];
                int endeHin = hinweg[i * 2 + 1];
                int startRueck = rueckweg[i * 2 + 1];

                int normiertesEndeHin;
                if (endeHin < startHin) {
                    normiertesEndeHin = endeHin + 60;
                } else {
                    normiertesEndeHin = endeHin;
                }

                if (startRueck <= normiertesEndeHin && startRueck >= startHin) {
                    kollisionen[i] = "  x";
                }
            }
        }
        dto.setKollisionen(kollisionen);
        return dto;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean istZeitueberlappend(int startHin, int endeHin, int startRueck, int endeRueck) {
        int lenHin = (endeHin - startHin + 60) % 60;
        int lenRueck = (endeRueck - startRueck + 60) % 60;

        int distHinToRueck = (startRueck - startHin + 60) % 60;
        int distRueckToHin = (startHin - startRueck + 60) % 60;

        return distHinToRueck < lenHin || distRueckToHin < lenRueck;
    }
}