import java.io.File;
import Algorithmen.Algorithmus;
import Algorithmen.BeidseitigAlg;
import Algorithmen.EinfachAlg;
import Algorithmen.EinseitigAlg;
import io.Exceptionhandler;
import io.Inputhandler;
import io.Outputhandler;
import model.DataStructure;

/**
 * Hauptklasse der Anwendung zur Berechnung von Zugfahrplänen auf eingleisigen Strecken. Diese Klasse steuert den
 * gesamten Programmfluss: Einlesen der Eingabedaten, Ausführen der drei Algorithmen (Einfach, Einseitig, Beidseitig)
 * und Generieren der Ausgabedatei.
 *
 * @author Felix Vauth
 * @version 1.0
 */
public class Main {

    /**
     * Einstiegspunkt der Anwendung. Erwartet den Dateipfad zur Eingabedatei (ohne Dateiendung) als erstes
     * Kommandozeilenargument. Bei fehlendem Argument wird das Programm mit einer Fehlermeldung beendet.
     *
     * @param args Kommandozeilenargumente; {@code args[0]} muss den Dateipfad enthalten
     */
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            Exceptionhandler.handle("Kommandozeilenargument für den Dateipfad fehlt.");
        } else {
            Exceptionhandler.setInputFile(args[0]);
        }
        try {
            Main app = new Main();
            app.run(args[0]);
        } catch (Exception e) {
            Exceptionhandler.handle(e, "Unerwarteter Fehler im Programmablauf.");
        }
    }

    /**
     * Führt die Hauptlogik der Anwendung aus. Liest die Eingabedatei ein, wendet alle drei Algorithmen auf separate
     * Kopien der Datenstruktur an und schreibt die Ergebnisse in die Ausgabedatei.
     *
     * @param filePath der Pfad zur Eingabedatei ohne Dateiendung (.txt wird angehängt)
     */
    private void run(String filePath) {
        Inputhandler in = new Inputhandler();
        Outputhandler out = new Outputhandler();

        File file = new File(filePath);
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
