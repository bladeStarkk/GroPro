package Algorithmen;

import model.DataStructure;

public interface Algorithmus {

    DataStructure algorithmus(DataStructure dto);

    boolean hatKollision(DataStructure dto);

    boolean istZeitueberlappend(int startHin, int endeHin, int startRueck);
}
