package Algorithmen;

import model.DataStructure;

public class EinseitigAlg extends Algorithmus {

    public DataStructure dto;

    @Override
    public DataStructure algorithmus(DataStructure dto) {

        int anzahl = dto.getAnzahl();
        int[] hinweg = dto.getHinweg();
        int[] rueckweg = dto.getRueckweg();

        int[] warteZeitenRueck = dto.getWartezeitRueck();

        if (hatKollision(dto)) {

        }

        return dto;
    }
}