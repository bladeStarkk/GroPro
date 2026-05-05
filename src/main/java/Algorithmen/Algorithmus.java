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

            if (istZeitueberlappend(startHin, sicherheitHin, startRueck)) {
                return true;
            }
        }
        return false;
    }

    private boolean istZeitueberlappend(int startHin, int endeHin, int startRueck) {
        int normiertesEndeHin;
        if (endeHin < startHin) {
            normiertesEndeHin = endeHin + 60;
        } else {
            normiertesEndeHin = endeHin;
        }
        if(startRueck <= normiertesEndeHin && startRueck >= startHin) {
            return true;
        }
        return false;
    }

}
