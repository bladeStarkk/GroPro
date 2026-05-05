package Algorithmen;

import model.DataStructure;

public class EinfachAlg extends Algorithmus {

    public DataStructure dto;

    @Override
    public DataStructure algorithmus(DataStructure dto) {
        this.dto = dto;

        int anzahl = dto.getAnzahl();
        int[] hinweg = dto.getHinweg();
        int[] rueckweg = dto.getRueckweg();

        String[] kollisionen = new String[anzahl - 1];
        int wartezeitHin = 0; //ToDo
        int wartezeitRueck = 0; //ToDo
        int strafen = 0;

        if (hatKollision(dto)) {
            for (int i = 0; i < anzahl - 1; i++) {

                int startHin = hinweg[i] % 60;
                int endeHin = hinweg[i + 1] % 60;

                int startRueck = rueckweg[i + 1] % 60;
                int endeRueck = rueckweg[i] % 60;

                if (startHin < endeRueck && startRueck < endeHin) {
                    kollisionen[i] = " x";
                } else {
                    kollisionen[i] = "";
                }
            }
        }

        int gesamtdauerHin = -1;
        int gesamtdauerRueck = -1;

        for (int i = 0; i < dto.getAbstaende().length; i++) {
            gesamtdauerHin += dto.getAbstaende()[i] + 1;

            gesamtdauerRueck += dto.getAbstaende()[dto.getAbstaende().length - 1 - i] + 1;
        }

        // Ergebnisse im DTO abspeichern
        dto.setKollisionen(kollisionen);
        dto.setWartezeitHin(wartezeitHin); //ToDo
        dto.setWartezeitRueck(wartezeitRueck); //ToDo
        dto.setStrafen(strafen);

        dto.setGesamtdauerHin(gesamtdauerHin + dto.getWartezeitHin()); //ToDo
        dto.setGesamtdauerRueck(gesamtdauerRueck + dto.getWartezeitRueck()); //ToDo

        return this.dto;
    }
}