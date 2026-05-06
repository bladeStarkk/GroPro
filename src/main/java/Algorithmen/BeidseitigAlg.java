package Algorithmen;

import model.DataStructure;

/**
 * Algorithmus zur Kollisionsauflösung durch beidseitiges Warten.
 * Bei diesem Ansatz wird zunächst der optimale Shift (0-59 Minuten) für den gesamten Rückweg gesucht,
 * um die initialen Kollisionen zu minimieren. Der Shift ist planmäßig und wird nicht als Wartezeit bestraft.
 * Danach werden die restlichen (unvermeidbaren) Wartezeiten möglichst gleichmäßig auf Hinfahrt und
 * Rückfahrt aufgeteilt (50/50-Verteilung).
 */
public class BeidseitigAlg implements Algorithmus {

    /**
     * Führt den Hauptalgorithmus zur Kollisionsauflösung aus.
     * Sucht den optimalen Shift für den Rückweg und teilt anfallende Wartezeiten
     * gleichmäßig zwischen Hinfahrt und Rückfahrt auf.
     *
     * @param dto Die aktuelle Datenstruktur mit den Fahrplaninformationen.
     * @return Die aktualisierte Datenstruktur mit dem konfliktfreien Fahrplan.
     */
    @Override
    public DataStructure algorithmus(DataStructure dto) {
        if (!hatKollision(dto)) {
            return dto;
        }

        int bestShift = 0;
        int minWartezeit = Integer.MAX_VALUE;

        for (int shift = 0; shift < 60; shift++) {
            DataStructure testDto = dto.deepCopy();

            int[] testRueck = testDto.getRueckweg().clone();
            for (int j = 0; j < testRueck.length; j++) {
                testRueck[j] = (testRueck[j] + shift) % 60;
            }
            testDto.setRueckweg(testRueck);

            EinseitigAlg testAlg = new EinseitigAlg();
            DataStructure resultDto = testAlg.algorithmus(testDto);

            int wartezeit = 0;
            if (resultDto.getWartezeitRueck() != null) {
                for (int w : resultDto.getWartezeitRueck()) {
                    wartezeit += w;
                }
            }

            if (wartezeit < minWartezeit) {
                minWartezeit = wartezeit;
                bestShift = shift;
            }
        }

        int[] bestShiftRueckweg = dto.getRueckweg().clone();
        for (int j = 0; j < bestShiftRueckweg.length; j++) {
            bestShiftRueckweg[j] = (bestShiftRueckweg[j] + bestShift) % 60;
        }
        dto.setRueckweg(bestShiftRueckweg);

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

        int zeitRueck = (letzteAnkunftHin + 1 + bestShift) % 60;

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
     * Prüft, ob im aktuellen Fahrplan Kollisionen auf eingleisigen Strecken vorliegen.
     *
     * @param dto Die aktuelle Datenstruktur mit den Fahrplaninformationen.
     * @return true, wenn eine Kollision existiert, andernfalls false.
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
     * Überprüft, ob sich zwei Zeitintervalle innerhalb eines 60-Minuten-Rasters überschneiden.
     *
     * @param startHin Startzeit der Hinfahrt.
     * @param endeHin  Endzeit der Hinfahrt.
     * @param startRueck Startzeit der Rückfahrt.
     * @param endeRueck  Endzeit der Rückfahrt.
     * @return true, wenn die Intervalle überlappen, andernfalls false.
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