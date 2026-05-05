package Algorithmen;

import model.DataStructure;

public class EinseitigAlg extends Algorithmus {

    public DataStructure dto;

    @Override
    public DataStructure algorithmus(DataStructure dto) {
        this.dto = dto;
        // Implementation for "Eins" logic
        return this.dto;
    }
}
