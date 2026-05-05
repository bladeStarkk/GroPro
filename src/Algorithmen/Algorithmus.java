package Algorithmen;

import model.DataStructure;

public abstract class Algorithmus {

    public abstract DataStructure algorithmus(DataStructure dto);

    public boolean hatKollision(DataStructure dto) {

        for (int i = 0; i < dto.getAnzahl() - 1; i++) {

            int startHin = dto.getHinweg()[i * 2 + 1];
            int endeHin = dto.getHinweg()[i * 2 + 2];

            int sicherheitHin = (endeHin + 1) % 60;

            int startRueck = dto.getRueckweg()[i * 2 + 1];
            int endeRueck = dto.getRueckweg()[i * 2 + 2];

            if (istZeitueberlappend(startHin, sicherheitHin, startRueck, endeRueck)) {
                return true;
            }
        }
        return false;
    }

    private boolean istZeitueberlappend(int startHin, int endeHin, int startRueck, int endeRueck) {
        int normiertesEndeHin;
        if (endeHin < startHin) {
            normiertesEndeHin = endeHin + 60;
        } else {
            normiertesEndeHin = endeHin;
        }
        int[] zeitVerschiebungen = {0, 60, -60};

        for (int verschiebung : zeitVerschiebungen) {
            int verschobenerStartRueck = startRueck + verschiebung;
            int verschobenesEndeRueck = (endeRueck < startRueck) ? endeRueck + 60 + verschiebung : endeRueck + verschiebung;

            if (!(verschobenerStartRueck >= normiertesEndeHin || verschobenesEndeRueck <= startHin)) {
                return true;
            }
        }
        return false;
    }

}
