package Algorithmen;

import model.DataStructure;

public class BeidseitigAlg implements Algorithmus {

    public DataStructure dto;

    @Override
    public DataStructure algorithmus(DataStructure dto) {
        int anzahl = dto.getAnzahl();
        int[] warteZeitenRueck = new int[anzahl];
        this.dto = dto;

        if (!hatKollision(dto)) {
            return dto;
        }

        // Besten Shift (0–59) finden, der die minimale Gesamtwartezeit ergibt
        int bestShift = 0;
        int bestWartezeit = Integer.MAX_VALUE;

        for (int shift = 0; shift < 60; shift++) {
            DataStructure testDto = dto.deepCopy();
            int[] rueckweg = testDto.getRueckweg();
            for (int j = 0; j < rueckweg.length; j++) {
                rueckweg[j] = (rueckweg[j] + shift) % 60;
            }
            testDto.setRueckweg(rueckweg);

            int gesamtWartezeit = berechneRestWartezeit(warteZeitenRueck);

            if (gesamtWartezeit < bestWartezeit) {
                bestWartezeit = gesamtWartezeit;
                bestShift = shift;
            }
        }

        // Shift anwenden
        int[] rueckweg = dto.getRueckweg();
        for (int j = 0; j < rueckweg.length; j++) {
            rueckweg[j] = (rueckweg[j] + bestShift) % 60;
        }
        dto.setRueckweg(rueckweg);

        // Restkollisionen per einseitigem Warten lösen
        int zusatzWartezeit = loeseMitEinseitigemWarten(dto);
        int gesamtWartezeit = bestShift + zusatzWartezeit;

        // Wartezeit optimal aufteilen: minimiert hin² + rück²
        int wartezeitHin = gesamtWartezeit / 2;
        int wartezeitRueck = gesamtWartezeit - wartezeitHin;

        int[] warteHin = dto.getWartezeitHin() != null ? dto.getWartezeitHin() : new int[anzahl];
        int[] warteRueck = dto.getWartezeitRueck() != null ? dto.getWartezeitRueck() : new int[anzahl];

        warteHin[anzahl - 1] = wartezeitHin;
        warteRueck[anzahl - 1] = wartezeitRueck;

        dto.setWartezeitHin(warteHin);
        dto.setWartezeitRueck(warteRueck);

        dto.setGesamtdauerHin(dto.getGesamtdauerHin() + wartezeitHin);
        dto.setGesamtdauerRueck(dto.getGesamtdauerRueck() + wartezeitRueck);
        dto.setStrafen(wartezeitHin * wartezeitHin + wartezeitRueck * wartezeitRueck);

        return dto;
    }

    private int berechneRestWartezeit(int[] warteZeitenRueck) {
        int wartezeit = 0;
        for (int i = 0; i < warteZeitenRueck.length; i++) {
            wartezeit += warteZeitenRueck[i];
        }
        return wartezeit;
    }

    private int loeseMitEinseitigemWarten(DataStructure dto) {
        int gesamtZusatz = 0;
        while (hatKollision(dto)) {
            int anzahl = dto.getAnzahl();
            int[] hinweg = dto.getHinweg();
            int[] rueckweg = dto.getRueckweg();
            String[] kollisionen = new String[anzahl - 1];

            for (int i = 0; i < anzahl - 1; i++) {
                int startHin = hinweg[i * 2];
                int endeHin = hinweg[i * 2 + 1];
                int startRueck = rueckweg[i * 2 + 1];

                int normiertesEndeHin = (endeHin < startHin) ? endeHin + 60 : endeHin;
                if (startRueck <= normiertesEndeHin && startRueck >= startHin) {
                    kollisionen[i] = "x";
                }
            }

            for (int i = kollisionen.length - 1; i >= 0; i--) {
                if (kollisionen[i] != null) {

                    int temp;

                    if (hinweg[2 * i + 1] < rueckweg[2 * i + 2]) {
                        temp = hinweg[2 * i + 1] + 60;
                    } else {
                        temp = hinweg[2 * i + 1];
                    }


                    int diff = temp - rueckweg[2 * i + 2] + 2;
                    gesamtZusatz += diff;

                    for (int j = 2 * i + 1; j >= 0; j--) {
                        rueckweg[j] = (rueckweg[j] + diff) % 60;
                    }
                    dto.setRueckweg(rueckweg);
                    break;
                }
            }
        }
        return gesamtZusatz;
    }

    @Override
    public boolean hatKollision(DataStructure dto) {
        for (int i = 0; i < dto.getAnzahl() - 1; i++) {
            int startHin = dto.getHinweg()[i * 2];
            int endeHin = dto.getHinweg()[i * 2 + 1];
            int sicherheitHin = (endeHin + 1) % 60;
            int startRueck = dto.getRueckweg()[i * 2 + 1];

            if (istZeitueberlappend(startHin, sicherheitHin, startRueck)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean istZeitueberlappend(int startHin, int endeHin, int startRueck) {
        int normiertesEndeHin = (endeHin < startHin) ? endeHin + 60 : endeHin;
        return startRueck < normiertesEndeHin && startRueck > startHin;
    }
}