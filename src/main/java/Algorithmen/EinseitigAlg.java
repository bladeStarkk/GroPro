package Algorithmen;

import model.DataStructure;

/**
 * Algorithmus zur Kollisionsauflösung durch einseitiges Warten. Bei diesem Ansatz warten ausschließlich die Rückfahrten
 * an Bahnhöfen, um den Hinfahrten Vorrang zu gewähren. Die Hinfahrt bleibt unverändert, während die Rückfahrt verzögert
 * wird.
 *
 * @see Algorithmus
 */
public class EinseitigAlg implements Algorithmus {

    /**
     * {@inheritDoc} Löst Kollisionen auf, indem die Rückfahrt an den betroffenen Bahnhöfen wartet. Die Wartezeiten
     * werden im Array {@code wartezeitRueck} gespeichert und die Strafen als Summe der quadrierten Wartezeiten
     * berechnet.
     */
    @Override
    public DataStructure algorithmus(DataStructure dto) {
        int anzahl = dto.getAnzahl();
        int[] warteZeitenRueck = new int[anzahl];
        int[] hinweg = dto.getHinweg();
        int[] rueckweg = dto.getRueckweg();
        int sicherheitszeit = dto.getSicherheitszeit();

        while (hatKollision(dto)) {
            String[] kollisionen = new String[anzahl - 1];

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

            for (int i = kollisionen.length - 1; i >= 0; i--) {
                if (kollisionen[i] != null) {
                    int sicherheitHin = (hinweg[i * 2 + 1] + sicherheitszeit) % 60;
                    int startRueck = rueckweg[i * 2 + 1];

                    int diff = (sicherheitHin - startRueck + 60) % 60;
                    if (diff == 0) {
                        diff = 1;
                    }

                    warteZeitenRueck[i + 1] += diff;

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