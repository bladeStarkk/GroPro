import Algorithmen.Algorithmus;
import Algorithmen.BeidseitigAlg;
import Algorithmen.EinfachAlg;
import Algorithmen.EinseitigAlg;
import io.Inputhandler;
import io.Outputhandler;
import model.DataStructure;

import java.io.File;

public class Main {

    private Inputhandler in;

    private Outputhandler out;

    private Algorithmus algo;

    private final String filePath = "../input/Beispiel_IHK_1.in";

    static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    private void run() {
        in = new Inputhandler();
        out = new Outputhandler();

        File file = new File(filePath);
        DataStructure dto = in.createDto(file);

        Algorithmus[] strategien = {new EinfachAlg(), new EinseitigAlg(), new BeidseitigAlg()};
        DataStructure[] ergebnisse = new DataStructure[strategien.length];

        for (int i = 0; i < strategien.length; i++) {
            DataStructure dtoKopie = dto.deepCopy();

            ergebnisse[i] = strategien[i].algorithmus(dtoKopie);
        }
        Outputhandler out = new Outputhandler();
        out.createOutput(ergebnisse[0], ergebnisse[1], ergebnisse[2], filePath);
    }
}
