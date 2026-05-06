package io;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import model.DataStructure;

/**
 * Handler zur Ausgabe der berechneten Fahrpläne in eine Textdatei. Diese Klasse erstellt eine formatierte Ausgabedatei
 * mit den Ergebnissen aller drei Algorithmen (Einfach, Einseitig, Beidseitig) sowie den zugehörigen Metriken wie
 * Wartezeiten und Strafpunkten.
 *
 * @see DataStructure
 */
public class Outputhandler {

    /**
     * Erstellt die Ausgabedatei mit den Fahrplänen aller Strategien. Die Ausgabe enthält: Reproduktion der
     * Eingabedaten, Allgemeine Streckeninformationen, Tabellarische Fahrpläne für jede Strategie, Metriken pro
     * Strategie (Dauer, Wartezeiten, Strafen)
     *
     * @param dtoEinfach Ergebnis des einfachen Algorithmus
     * @param dtoEinseitig Ergebnis des einseitigen Warten-Algorithmus
     * @param dtoBeidseitig Ergebnis des beidseitigen Warten-Algorithmus
     * @param inputDatei Pfad der Eingabedatei zur Ableitung des Ausgabenamens
     */
    public void createOutput(DataStructure dtoEinfach, DataStructure dtoEinseitig, DataStructure dtoBeidseitig,
            String inputDatei) {
        String outputDateiName;
        int dotIndex = inputDatei.lastIndexOf('.');

        String output = inputDatei.replaceFirst("^input/", "output/");
        if (dotIndex == -1) {
            outputDateiName = output + "_aus.txt";
        } else {
            String name = output.substring(0, dotIndex+1);
            String endung = output.substring(dotIndex+1);
            outputDateiName = name + "_aus" + endung;
        }

        try (PrintWriter writer = new PrintWriter(outputDateiName)) {
            writer.println("Strecke:");
            writer.println(String.join(" ", dtoEinfach.getStrecke()));
            writer.println();

            writer.println("Abstaende:");
            StringBuilder abstaendeStr = new StringBuilder();
            for (int abstand : dtoEinfach.getAbstaende()) {
                abstaendeStr.append(abstand).append(" ");
            }
            writer.println(abstaendeStr.toString().trim());
            writer.println();

            writer.println("Start Hinfahrt:");
            writer.println(dtoEinfach.getStart());
            writer.println();

            writer.println("Anzahl Bahnhöfe : " + dtoEinfach.getAnzahl());
            writer.println("Mindestdauer    : " + dtoEinfach.getMinDauer());
            writer.println();

            writer.println("Einfache Fahrt:");
            druckeTabelle(writer, dtoEinfach);

            writer.println("Einseitiges Warten:");
            druckeTabelle(writer, dtoEinseitig);

            writer.println("Beidseitiges Warten:");
            druckeTabelle(writer, dtoBeidseitig);

            System.out.println("Output erfolgreich generiert: " + outputDateiName);
            System.out.println(Arrays.toString(dtoEinfach.getKollisionen()));

        } catch (IOException e) {
            ExceptionHandler.handle(e, "Fehler beim Schreiben der Ausgabedatei");
        }
    }

    /**
     * Druckt den tabellarischen Fahrplan einer Strategie. Die Tabelle zeigt Ankunfts- und Abfahrtszeiten sowie
     * Wartezeiten für Hin- und Rückfahrt an allen Bahnhöfen. Kollisionen werden mit einem "x" markiert.
     *
     * @param writer der PrintWriter für die Ausgabedatei
     * @param dto die Datenstruktur mit den Fahrplandaten
     */
    private void druckeTabelle(PrintWriter writer, DataStructure dto) {
        int anzahl = dto.getStrecke().length;
        String colFormat = "%-6s";

        int[] hinweg = dto.getHinweg();
        int[] rueckweg = dto.getRueckweg();
        int[] warteHin = dto.getWartezeitHin();
        int[] warteRueck = dto.getWartezeitRueck();

        writer.printf("%-4s", "An");
        writer.printf(colFormat, "");
        for (int i = 1; i < anzahl; i++) {
            writer.printf(colFormat, formatZeit(hinweg != null ? hinweg[2 * i - 1] : -1));
        }
        writer.println();

        writer.printf("%-3s", "Wa");
        for (int i = 0; i < anzahl; i++) {
            writer.printf(colFormat, formatWartezeit(warteHin != null ? warteHin[i] : 0));
        }
        writer.println();

        writer.printf("%-4s", "Ab");
        for (int i = 0; i < anzahl - 1; i++) {
            writer.printf(colFormat, formatZeit(hinweg != null ? hinweg[2 * i] : -1));
        }
        writer.printf(colFormat, "");
        writer.println();

        writer.printf("%-4s", "");
        for (int i = 0; i < anzahl; i++) {
            String name = dto.getStrecke()[i];
            if (i < anzahl - 1) {
                if (dto.getKollisionen()[i] == null) {
                    name += "  ";
                } else {
                    name += dto.getKollisionen()[i];
                }
            }
            writer.printf(colFormat, name);
        }
        writer.println();

        writer.printf("%-4s", "Ab");
        writer.printf(colFormat, "");
        for (int i = 1; i < anzahl; i++) {
            writer.printf(colFormat, formatZeit(rueckweg != null ? rueckweg[2 * i - 1] : -1));
        }
        writer.println();

        writer.printf("%-3s", "Wa");
        for (int i = 0; i < anzahl; i++) {
            writer.printf(colFormat, formatWartezeit(warteRueck != null ? warteRueck[i] : 0));
        }
        writer.println();

        writer.printf("%-4s", "An");
        for (int i = 0; i < anzahl - 1; i++) {
            writer.printf(colFormat, formatZeit(rueckweg != null ? rueckweg[2 * i] : -1));
        }
        writer.printf(colFormat, "");
        writer.println();
        writer.println();

        int summeWaHin = 0;
        int summeWaRueck = 0;

        if (warteHin != null) {
            for (int w : warteHin) {
                summeWaHin += w;
            }
        }
        if (warteRueck != null) {
            for (int w : warteRueck) {
                summeWaRueck += w;
            }
        }

        writer.println("Gesamtdauer Hinfahrt, Rückfahrt       : " + dto.getGesamtdauerHin() + ", "
                + dto.getGesamtdauerRueck());
        writer.println("Summe Wartezeiten Hinfahrt, Rückfahrt : " + summeWaHin + ", " + summeWaRueck);
        writer.println("Summe Strafen                         : " + dto.getStrafen());
        writer.println();
    }

    /**
     * Formatiert eine Minutenangabe als zweistellige Zahl.
     *
     * @param minuten die Minutenangabe (wird modulo 60 genommen)
     * @return zweistelliger String oder leerer String bei negativem Wert
     */
    private String formatZeit(int minuten) {
        if (minuten < 0) {
            return "";
        }
        return String.format("%02d", minuten % 60);
    }

    /**
     * Formatiert eine Wartezeit in Klammern.
     *
     * @param wartezeit die Wartezeit in Minuten
     * @return formatierter String "(MM)" oder leerer String bei Wert kleiner gleich 0
     */
    private String formatWartezeit(int wartezeit) {
        if (wartezeit <= 0) {
            return "";
        }
        return String.format("(%02d)", wartezeit);
    }
}