package Algorithmen;

import model.DataStructure;

public class BeidseitigAlg implements Algorithmus {

    public DataStructure dto;

    @Override
    public DataStructure algorithmus(DataStructure dto) {
        this.dto = dto;
        // Implementation for "Beide" logic
        return this.dto;
    }

    @Override
    public boolean hatKollision(DataStructure dto) {
        return false;
    }

    @Override
    public boolean istZeitueberlappend(int startHin, int endeHin, int startRueck) {
        return false;
    }
}
