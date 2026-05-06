package Algorithmen;

import model.DataStructure;

/**
 * Algorithmus zur Kollisionsauflösung durch beidseitiges Warten. Bei diesem Ansatz werden die Wartezeiten möglichst
 * gleichmäßig auf Hinfahrt und Rückfahrt aufgeteilt (50/50-Verteilung). Dies minimiert die quadratischen Strafkosten im
 * Vergleich zum einseitigen Warten.
 *
 * @see Algorithmus
 * @see EinseitigAlg
 */
public class BeidseitigAlg implements Algorithmus {

    /**
     * {@inheritDoc} Nutzt zunächst den {@link EinseitigAlg} auf einer Kopie, um die benötigten Wartezeiten zu
     * ermitteln. Diese werden dann hälftig auf Hin- und Rückfahrt verteilt. Die Strafen werden als Summe der
     * quadrierten Wartezeiten beider Richtungen berechnet.
     */
    @Override
    public DataStructure algorithmus(DataStructure dto) {
        if (!hatKollision(dto)) {
            return dto;
        }

        int anzahl = dto.getAnzahl();
        int umstiegszeit = dto.getUmstiegszeit();
        int sicherheitsZeit = dto.getSicherheitszeit();

        DataStructure copyDto = dto.deepCopy();
        EinseitigAlg einseitig = new EinseitigAlg();
        copyDto = einseitig.algorithmus(copyDto);

        int[] einseitigWarte = copyDto.getWartezeitRueck();

        int[] warteHin = new int[anzahl];
        int[] warteRueck = new int[anzahl];
        int gesamtWartezeitHin = 0;
        int gesamtWartezeitRueck = 0;

        for (int i = 0; i < anzahl; i++) {
            int w = einseitigWarte[i];
            warteHin[i] = w / 2;
            warteRueck[i] = w - warteHin[i];

            gesamtWartezeitHin += warteHin[i];
            gesamtWartezeitRueck += warteRueck[i];
        }

        int[] hinweg = dto.getHinweg().clone();
        int zeitHin = hinweg[0];

        for (int i = 0; i < anzahl - 1; i++) {
            zeitHin = (zeitHin + dto.getAbstaende()[i]) % 60;
            hinweg[i * 2 + 1] = zeitHin;

            if (i + 1 < anzahl - 1) {
                zeitHin = (zeitHin + umstiegszeit + warteHin[i + 1]
                        + Math.max(0, (umstiegszeit - sicherheitsZeit)) % 60);
                hinweg[(i + 1) * 2] = zeitHin;
            }
        }
        dto.setHinweg(hinweg);
        dto.setWartezeitHin(warteHin);

        int[] rueckweg = dto.getRueckweg().clone();

        int letzteAnkunftHin = hinweg[(anzahl - 2) * 2 + 1];
        int zeitRueck = (letzteAnkunftHin + 1) % 60;

        for (int i = anzahl - 2; i >= 0; i--) {
            if (i < anzahl - 2) {
                zeitRueck = (zeitRueck + umstiegszeit + warteRueck[i + 1] + Math.max(0, umstiegszeit - sicherheitsZeit))
                        % 60;
            }
            rueckweg[i * 2 + 1] = zeitRueck;

            zeitRueck = (zeitRueck + dto.getAbstaende()[i]) % 60;
            rueckweg[i * 2] = zeitRueck;
        }
        dto.setRueckweg(rueckweg);
        dto.setWartezeitRueck(warteRueck);

        dto.setGesamtdauerHin(dto.getMinDauer() + gesamtWartezeitHin);
        dto.setGesamtdauerRueck(dto.getMinDauer() + gesamtWartezeitRueck);
        dto.setStrafen(gesamtWartezeitHin * gesamtWartezeitHin + gesamtWartezeitRueck * gesamtWartezeitRueck);

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
        // Diese Logik vergleicht die Block-Längen auf einem runden Ziffernblatt.
        // Sie ist immun gegen den 60-Minuten-Sprung (z.B. von 55 auf 05 Uhr).
        int lenHin = (endeHin - startHin + 60) % 60;
        int lenRueck = (endeRueck - startRueck + 60) % 60;

        int distHinToRueck = (startRueck - startHin + 60) % 60;
        int distRueckToHin = (startHin - startRueck + 60) % 60;

        return distHinToRueck < lenHin || distRueckToHin < lenRueck;
    }
}