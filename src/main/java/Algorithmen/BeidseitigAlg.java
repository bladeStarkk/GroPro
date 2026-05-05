package Algorithmen;

import model.DataStructure;

public class BeidseitigAlg extends Algorithmus {

    public DataStructure dto;

    @Override
    public DataStructure algorithmus(DataStructure dto) {
        this.dto = dto;
        // Implementation for "Beide" logic
        return this.dto;
    }
}
