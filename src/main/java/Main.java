import java.io.File;
import Algorithmen.Algorithmus;
import Algorithmen.BeidseitigAlg;
import Algorithmen.EinfachAlg;
import Algorithmen.EinseitigAlg;
import io.Inputhandler;
import io.Outputhandler;
import model.DataStructure;

/**
 * Die Hauptklasse der Anwendung, die den Programmfluss steuert.
 */
public class Main {


    /**
     * Startpunkt der Anwendung.
     * @param args Kommandozeilenargumente, erwartet den Dateipfad als erstes Argument.
     */
    public static void main(String[] args) {
        Main app = new Main();
        app.run(args[0]);
    }

    /**
     * Führt die Hauptlogik der Anwendung aus.
     * @param filePath Der Pfad der Eingabedatei ohne Dateiendung.
     */
    private void run(String filePath) {
        Inputhandler in = new Inputhandler();
        Outputhandler out = new Outputhandler();

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
