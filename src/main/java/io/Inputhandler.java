package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import model.DataStructure;

/**
 * Klasse zum Einlesen der Streckendaten und zur Initialisierung der grundlegenden Datenstrukturen.
 */
public class Inputhandler {

    private DataStructure dto;

    /**
     * Liest die angegebene Eingabedatei ein und erstellt das Basis-DataStructure-Objekt
     * mit dem initialen (ungestörten) Fahrplan.
     *
     * @param file Die Eingabedatei, die die Streckeninformationen enthält.
     * @return Ein initialisiertes DataStructure-Objekt mit grundlegenden Fahrplänen.
     */
    public DataStructure createDto(File file) {
        dto = new DataStructure();

        try (Scanner scanner = new Scanner(file)) {
            // Zeile 1: "Strecke:" (ignorieren)
            scanner.nextLine();

            // Zeile 2: Bahnhöfe
            String streckenZeile = scanner.nextLine().trim();
            dto.setStrecke(streckenZeile.split(" "));
            dto.setAnzahl(dto.getStrecke().length);

            // Zeile 3: leer, Zeile 4: "Abstaende:" (ignorieren)
            scanner.nextLine();
            scanner.nextLine();

            // Zeile 5: Abstände
            String[] abstaendeStr = scanner.nextLine().trim().split(" ");
            int[] abstaende = new int[abstaendeStr.length];
            int summeAbstaende = 0;
            for (int i = 0; i < abstaendeStr.length; i++) {
                abstaende[i] = Integer.parseInt(abstaendeStr[i]);
                summeAbstaende += abstaende[i];
            }
            dto.setAbstaende(abstaende);

            // Mindestdauer berechnen: Summe der Abstände + Haltezeiten an ZWISCHENbahnhöfen (Anzahl - 2)
            int haltezeiten = dto.getAnzahl() - 2;
            dto.setMinDauer(summeAbstaende + haltezeiten);

            // Zeile 6: leer, Zeile 7: "Start Hinfahrt:" (ignorieren)
            scanner.nextLine();
            scanner.nextLine();

            // Zeile 8: Startzeit
            int start = Integer.parseInt(scanner.nextLine().trim());
            dto.setStart(start);

            // ==============================================================
            // NEU: ARRAYS INITIALISIEREN & GRUNDFAHRPLAN (HIN/RÜCK) BAUEN
            // ==============================================================
            int anzahl = dto.getAnzahl();

            int[] hinweg = new int[anzahl * 2 - 2];
            int[] rueckweg = new int[anzahl * 2 - 2];
            String[] kollisionen = new String[anzahl - 1];
            int[] wartezeitHin = new int[anzahl];
            int[] wartezeitRueck = new int[anzahl];

            // 1. Hinweg berechnen (Reguläre Fahrzeit + 1 Min Haltezeit)
            hinweg[0] = start; // Abfahrt am ersten Bahnhof (z.B. A)
            int temp = start;

            for (int i = 1; i < anzahl-1; i++) {
                int num = (temp + dto.getAbstaende()[i - 1]) % 60;
                hinweg[2 * i - 1] = num;
                hinweg[2 * i] = (num + 1) % 60;

                temp = hinweg[2 * i];
            }
            hinweg[hinweg.length - 1] =
                    (hinweg[hinweg.length - 2] + dto.getAbstaende()[dto.getAbstaende().length - 1]) % 60;

            int startRueck = (hinweg[hinweg.length - 1] + 1) % 60;

            rueckweg[rueckweg.length-1] = startRueck;
            int tempRueck = startRueck;

            for (int i = anzahl - 2; i > 0; i--) {
                int ankunft = (tempRueck + dto.getAbstaende()[i]) % 60;
                rueckweg[2 * i] = ankunft;

                int abfahrt = (ankunft + 1) % 60;
                rueckweg[2 * i - 1] = abfahrt;
                tempRueck = abfahrt;
            }
            rueckweg[0] = (tempRueck + dto.getAbstaende()[0]) % 60;

            // 3. Kollisions-Array mit leeren Strings füllen (verhindert "null" bei der Ausgabe)
            java.util.Arrays.fill(kollisionen, "");

            int gesamtWartezeitHin = 0;
            int gesamtWartezeitRueck = 0;
            int gesamtdauerHin = -1;
            int gesamtdauerRueck = -1;

            for (int i = 0; i < dto.getAbstaende().length; i++) {
                gesamtdauerHin += dto.getAbstaende()[i] + 1;

                gesamtdauerRueck += dto.getAbstaende()[dto.getAbstaende().length - 1 - i] + 1;
            }
            if (dto.getWartezeitHin() != null) {
                for (int i = 0; i < dto.getWartezeitHin().length; i++) {
                    gesamtWartezeitHin += dto.getWartezeitHin()[i];
                }
            }
            if (dto.getWartezeitRueck() != null) {
                for (int i = 0; i < dto.getWartezeitHin().length; i++) {
                    gesamtWartezeitRueck += dto.getWartezeitRueck()[i];
                }
            }

            dto.setGesamtdauerHin(gesamtdauerHin + gesamtWartezeitHin);
            dto.setGesamtdauerRueck(gesamtdauerRueck + gesamtWartezeitRueck);


            // 4. Alle vorbereiteten Arrays im DTO speichern
            dto.setHinweg(hinweg);
            dto.setRueckweg(rueckweg);
            dto.setKollisionen(kollisionen);
            dto.setWartezeitHin(wartezeitHin);
            dto.setWartezeitRueck(wartezeitRueck);
            dto.setStrafen(0);

        } catch (FileNotFoundException e) {
            System.err.println("Datei nicht gefunden: " + e.getMessage());
        }

        return dto;
    }
}