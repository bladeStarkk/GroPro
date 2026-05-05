import java.io.File;
import Algorithmen.Algorithmus;
import Algorithmen.BeidseitigAlg;
import Algorithmen.EinfachAlg;
import Algorithmen.EinseitigAlg;
import io.Inputhandler;
import io.Outputhandler;
import model.DataStructure;

public class Main {

    private Inputhandler in;

    private Outputhandler out;

    public static void main(String[] args) {
        Main app = new Main();
        app.run(args[0]);
    }

    private void run(String filePath) {
        in = new Inputhandler();
        out = new Outputhandler();

        File file = new File(filePath + ".txt");
        DataStructure dto = in.createDto(file);

        Algorithmus[] strategien = {new EinfachAlg(), new EinseitigAlg(), new BeidseitigAlg()};
        DataStructure[] ergebnisse = new DataStructure[strategien.length];

        for (int i = 0; i < strategien.length; i++) {
            DataStructure dtoKopie = dto.deepCopy();

            ergebnisse[i] = strategien[i].algorithmus(dtoKopie);
        }
        out.createOutput(ergebnisse[0], ergebnisse[1], ergebnisse[2], filePath);
    }
}
