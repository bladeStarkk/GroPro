package Algorithmen;

import model.DataStructure;

public class EinseitigAlg implements Algorithmus {

    public DataStructure dto;

    @Override
    public DataStructure algorithmus(DataStructure dto) {
        while (hatKollision(dto) == true) {
            int anzahl = dto.getAnzahl();
            int[] hinweg = dto.getHinweg();
            int[] rueckweg = dto.getRueckweg();

            int[] warteZeitenRueck = new int[anzahl];
            if(dto.getWartezeitRueck()!=null) {
                warteZeitenRueck = dto.getWartezeitRueck();
            }

            String[] kollisionen = new String[anzahl - 1];
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
            for (int i = kollisionen.length - 1; i >= 0; i--) {
                if(kollisionen[i] != null) {
                    int temp;

                    if (hinweg[2*i+1] < rueckweg[2*i+2]) temp = hinweg[2*i+1] + 60;
                    else temp = hinweg[2*i+1];

                    int diff = temp - rueckweg[2*i+2];
                    warteZeitenRueck[i+1] += diff;

                    for (int j = 2*i+1; j >= 0; j--) {
                        rueckweg[j] = (rueckweg[j] + diff) % 60;
                    }
                    dto.setRueckweg(rueckweg);
                    dto.setWartezeitRueck(warteZeitenRueck);
                    break;
                }
            }
        }
        int wartezeitRueckGesamt = 0;
        for (int i = 0; i < dto.getWartezeitRueck().length; i++) {
            wartezeitRueckGesamt += dto.getWartezeitRueck()[i];
        }
        dto.setGesamtdauerRueck(dto.getGesamtdauerRueck() + wartezeitRueckGesamt);
        dto.setStrafen(wartezeitRueckGesamt*wartezeitRueckGesamt);

        return dto;
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
        int normiertesEndeHin;
        if (endeHin < startHin) {
            normiertesEndeHin = endeHin + 60;
        } else {
            normiertesEndeHin = endeHin;
        }
        if(startRueck < normiertesEndeHin && startRueck > startHin) {
            return true;
        }
        return false;
    }
}