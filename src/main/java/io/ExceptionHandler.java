package io;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Globale Fehlerbehandlungsklasse für kritische Programmfehler. Diese Klasse schreibt Fehlerberichte in eine
 * Ausgabedatei und beendet das Programm kontrolliert. Sie stellt sicher, dass auch bei unerwarteten Fehlern eine
 * nachvollziehbare Dokumentation erstellt wird.
 *
 * @author Felix Vauth
 * @version 1.0
 */
public class ExceptionHandler {

    /** Name der Ausgabedatei für Fehlerberichte, falls keine Eingabedatei vorhanden ist. */
    private static String outputDateiName = "output/fehler_ausgabe.txt";

    /**
     * Setzt den Namen der Ausgabedatei basierend auf der Eingabedatei. Diese Methode sollte zu Beginn des Programms
     * aufgerufen werden, um die Fehlerdatei im selben Kontext wie die reguläre Ausgabe zu erstellen.
     *
     * @param inputDatei der Pfad der Eingabedatei
     */
    public static void setInputFile(String inputDatei) {
        if(inputDatei == null || inputDatei.trim().isEmpty()) {
            return;
        }
        int dotIndex = inputDatei.lastIndexOf('.');
        String output = inputDatei.replaceFirst("^input/", "output/");

        if (dotIndex == -1) {
            outputDateiName = output + "_aus.txt";
        } else {
            String name = output.substring(0, dotIndex);
            String endung = output.substring(dotIndex);
            outputDateiName = name + "_output" + endung;
        }
    }

    /**
     * Behandelt einen kritischen Fehler und beendet das Programm. Der Fehler wird auf der Konsole ausgegeben und ein
     * detaillierter Fehlerbericht in die Ausgabedatei geschrieben. Anschließend wird das Programm mit Exit-Code 1
     * beendet.
     *
     * @param e die aufgetretene Exception (kann {@code null} sein)
     * @param context eine Beschreibung des Kontexts, in dem der Fehler auftrat
     */
    public static void handle(Exception e, String context) {
        System.err.println("KRITISCHER FEHLER: " + context);
        if (e != null) {
            System.err.println("Fehlermeldung: " + e.getMessage());
        }
        System.err.println("Programmabbruch. Fehlerbericht wird geschrieben in: " + outputDateiName);

        try (PrintWriter writer = new PrintWriter(outputDateiName)) {
            writer.println("=== FEHLERBERICHT ===");
            writer.println("Kontext: " + context);

            if (e != null) {
                writer.println("Exception: " + e);
                writer.println("Stacktrace:");
                e.printStackTrace(writer);
            }
            writer.println("=====================");

        } catch (IOException ioException) {
            System.err.println("Zusätzlicher Fehler beim Schreiben der Datei " + outputDateiName + ": "
                    + ioException.getMessage());
        }

        System.exit(1);
    }

    /**
     * Behandelt einen Fehler ohne zugehörige Exception. Überladene Methode für Fälle, in denen nur eine Fehlermeldung
     * ohne Exception-Objekt vorliegt.
     *
     * @param errorMessage die Fehlermeldung
     */
    public static void handle(String errorMessage) {
        handle(null, errorMessage);
    }
}