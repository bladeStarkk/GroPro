package Algorithmen;

import model.DataStructure;

public class EinfachAlg extends Algorithmus {

    public DataStructure dto;

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
}