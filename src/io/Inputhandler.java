package io;

import model.DataStructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Inputhandler {

    private DataStructure dto;

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

            int[] hinweg = new int[anzahl*2];
            int[] rueckweg = new int[anzahl*2];
            String[] kollisionen = new String[anzahl - 1];
            int wartezeitHin = 0;
            int wartezeitRueck = 0;

            // 1. Hinweg berechnen (Reguläre Fahrzeit + 1 Min Haltezeit)
            hinweg[0] = start; // Abfahrt am ersten Bahnhof (z.B. A)
            int temp = start;

            for (int i = 1; i < anzahl; i++) {
                int num = (temp + dto.getAbstaende()[i - 1]) % 60;
                hinweg[2 * i - 1] = num;
                hinweg[2 * i] = (num + 1) % 60;

                temp = hinweg[2 * i];
            }

            int ankunftLetzterBahnhof = hinweg[2 * anzahl - 3];
            int startRueck = (ankunftLetzterBahnhof + 1) % 60;

            rueckweg[2 * (anzahl - 1)] = startRueck;
            int tempRueck = startRueck;

            for (int i = anzahl - 2; i >= 0; i--) {
                int ankunft = (tempRueck + dto.getAbstaende()[i]) % 60;
                rueckweg[2 * i + 1] = ankunft;

                int abfahrt = (ankunft + 1) % 60;
                rueckweg[2 * i] = abfahrt;
                tempRueck = abfahrt;
            }

            // 3. Kollisions-Array mit leeren Strings füllen (verhindert "null" bei der Ausgabe)
            for (int i = 0; i < kollisionen.length; i++) {
                kollisionen[i] = "";
            }

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