package io;

import model.DataStructure;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Outputhandler {

    // Die globale Variable 'dto' wird nicht mehr benötigt,
    // da wir die drei spezifischen DTOs direkt übergeben.

    public void createOutput(DataStructure dtoEinfach, DataStructure dtoEinseitig, DataStructure dtoBeidseitig, String inputDatei) {
        String outputDateiName;
        int dotIndex = inputDatei.lastIndexOf('.');

        // Namensgenerierung
        if (dotIndex == -1) {
            outputDateiName = inputDatei + "_output";
        } else {
            String name = inputDatei.substring(0, dotIndex);
            String endung = inputDatei.substring(dotIndex);
            outputDateiName = name + "_output" + endung;
        }

        try (PrintWriter writer = new PrintWriter(new File(outputDateiName))) {

            // --- 1. Eingabedaten reproduzieren ---
            // Wir nehmen einfach dtoEinfach für die Grunddaten, da diese bei allen gleich sind.
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

            // --- 2. Allgemeine Informationen ---
            writer.println("Anzahl Bahnhöfe: " + dtoEinfach.getAnzahl());
            writer.println("Mindestdauer   : " + dtoEinfach.getMinDauer());
            writer.println();

            // --- 3. Strategien ausgeben ---

            // 3.1 Einfache Fahrt
            writer.println("Einfache Fahrt:");
            druckeTabelle(writer, dtoEinfach);

            // 3.2 Einseitiges Warten
            writer.println("Einseitiges Warten:");
            druckeTabelle(writer, dtoEinseitig);

            // 3.3 Beidseitiges Warten
            writer.println("Beidseitiges Warten:");
            druckeTabelle(writer, dtoBeidseitig);

            System.out.println("Output erfolgreich generiert: " + outputDateiName);

        } catch (IOException e) {
            System.err.println("Kritischer Fehler beim Schreiben der Datei " + outputDateiName + ": " + e.getMessage());
        }
    }

    private void druckeTabelle(PrintWriter writer, DataStructure dto) {
        int anzahl = dto.getStrecke().length;
        String colFormat = "%-6s";

        // Arrays sicherheitshalber auslesen (Null-Checks)
        int[] hinweg = dto.getHinweg();
        int[] rueckweg = dto.getRueckweg();
        int[] warteHin = dto.getWartezeitHin();
        int[] warteRueck = dto.getWartezeitRueck();
        String[] kollisionen = dto.getKollisionen();

        // 1. Zeile: Ankunft Hinfahrt (ungerader Index: 2*i - 1)
        writer.printf("%-4s", "An");
        writer.printf(colFormat, ""); // Erster Bahnhof hat keine Ankunft
        for (int i = 1; i < anzahl; i++) {
            writer.printf(colFormat, formatZeit(hinweg != null ? hinweg[2 * i - 1] : -1));
        }
        writer.println();

        // 2. Zeile: Wartezeit Hinfahrt
        writer.printf("%-4s", "Wa");
        for (int i = 0; i < anzahl; i++) {
            writer.printf(colFormat, formatWartezeit(warteHin != null ? warteHin[i] : 0));
        }
        writer.println();

        // 3. Zeile: Abfahrt Hinfahrt (gerader Index: 2*i)
        writer.printf("%-4s", "Ab");
        for (int i = 0; i < anzahl - 1; i++) {
            writer.printf(colFormat, formatZeit(hinweg != null ? hinweg[2 * i] : -1));
        }
        writer.printf(colFormat, ""); // Letzter Bahnhof hat keine Abfahrt auf der Hinfahrt
        writer.println();

        // 4. Zeile: Bahnhöfe (inklusive Kollisionsmarker " x")
        writer.printf("%-4s", "");
        for (int i = 0; i < anzahl; i++) {
            String name = dto.getStrecke()[i];
            // Wenn es eine Kollision nach diesem Bahnhof gibt, füge das " x" an
            if (i < anzahl - 1 && kollisionen != null && !kollisionen[i].isEmpty()) {
                name += kollisionen[i];
            }
            writer.printf(colFormat, name);
        }
        writer.println();

        // 5. Zeile: Abfahrt Rückfahrt (gerader Index: 2*i)
        writer.printf("%-4s", "Ab");
        writer.printf(colFormat, ""); // Zielbahnhof der Hinfahrt (Index 0) hat keine Abfahrt auf der Rückfahrt
        for (int i = 1; i < anzahl; i++) {
            writer.printf(colFormat, formatZeit(rueckweg != null ? rueckweg[2 * i] : -1));
        }
        writer.println();

        // 6. Zeile: Wartezeit Rückfahrt
        writer.printf("%-4s", "Wa");
        for (int i = 0; i < anzahl; i++) {
            writer.printf(colFormat, formatWartezeit(warteRueck != null ? warteRueck[i] : 0));
        }
        writer.println();

        // 7. Zeile: Ankunft Rückfahrt (ungerader Index: 2*i + 1)
        writer.printf("%-4s", "An");
        for (int i = 0; i < anzahl - 1; i++) {
            writer.printf(colFormat, formatZeit(rueckweg != null ? rueckweg[2 * i + 1] : -1));
        }
        writer.printf(colFormat, ""); // Startbahnhof der Rückfahrt hat keine Ankunft
        writer.println();
        writer.println();

        // --- 8. Abschluss-Metriken ---
        // Wartezeiten aufsummieren
        int summeWaHin = 0;
        int summeWaRueck = 0;
        if (warteHin != null) for (int w : warteHin) summeWaHin += w;
        if (warteRueck != null) for (int w : warteRueck) summeWaRueck += w;

        writer.println("Gesamtdauer Hinfahrt, Rückfahrt       : " + dto.getGesamtdauerHin() + ", " + dto.getGesamtdauerRueck());
        writer.println("Summe Wartezeiten Hinfahrt, Rückfahrt : " + summeWaHin + ", " + summeWaRueck);
        writer.println("Summe Strafen                         : " + dto.getStrafen());
        writer.println();
    }

// --- Hilfsmethoden für die Formatierung ---

    private String formatZeit(int minuten) {
        if (minuten < 0) return "";
        return String.format("%02d", minuten % 60);
    }

    private String formatWartezeit(int wartezeit) {
        if (wartezeit <= 0) return "";
        return String.format("(%02d)", wartezeit);
    }
}