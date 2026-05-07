package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import model.DataStructure;

/**
 * Handler zum Einlesen von Streckendaten aus einer Textdatei. Diese Klasse liest die Eingabedatei im vorgegebenen
 * Format ein und erstellt eine initialisierte {@link DataStructure} mit dem Grundfahrplan für Hin- und Rückfahrt.
 */
public class Inputhandler {

    /** Die zu erstellende Datenstruktur. */
    private DataStructure dto;

    /**
     * Liest die Eingabedatei und erstellt die Basis-Datenstruktur. Das erwartete Dateiformat:
     * Strecke: A B C D E
     *
     * Abstaende: 1 2 3 4
     *
     * Start Hinfahrt: 10
     *
     * @param file die Eingabedatei mit Streckeninformationen
     * @return eine initialisierte {@link DataStructure} mit Grundfahrplan
     */
    public DataStructure createDto(File file) {
        dto = new DataStructure();

        try (Scanner scanner = new Scanner(file)) {

            // 1. PRÜFUNG: Ist die Datei komplett leer?
            if (!scanner.hasNextLine()) {
                Exceptionhandler.handle("Die Eingabedatei ist komplett leer: " + file.getPath());
            }

            scanner.nextLine(); // "Strecke:" überspringen

            // 2. PRÜFUNG: Ist die Liste der Bahnhöfe leer?
            String streckenZeile = scanner.nextLine().trim();
            if (streckenZeile.isEmpty()) {
                Exceptionhandler.handle("Fehler: Die Liste der Bahnhöfe (Strecke) ist leer.");
            }

            dto.setStrecke(streckenZeile.split(" "));
            dto.setAnzahl(dto.getStrecke().length);
            if(dto.getAnzahl() < 2) {
                Exceptionhandler.handle("Fehler: Es müssen mindestens 2 Bahnhöfe in der Strecke angegeben sein.");
            }
            scanner.nextLine(); // Leerzeile
            scanner.nextLine(); // "Abstaende:" überspringen

            // 3. PRÜFUNG: Ist die Liste der Abstände leer?
            String abstaendeZeile = scanner.nextLine().trim();
            if (abstaendeZeile.isEmpty()) {
                Exceptionhandler.handle("Fehler: Die Liste der Abstände ist leer.");
            }

            String[] abstaendeStr = abstaendeZeile.split(" ");

            // 4. PRÜFUNG: Anzahl Abstände = Anzahl Bahnhöfe - 1?
            if (abstaendeStr.length != dto.getAnzahl() - 1) {
                Exceptionhandler.handle("Logikfehler: Die Anzahl der Abstände (" + abstaendeStr.length +
                        ") muss genau der Anzahl der Bahnhöfe minus 1 (" + (dto.getAnzahl() - 1) + ") entsprechen.");
            }

            int[] abstaende = new int[abstaendeStr.length];
            int summeAbstaende = 0;

            int sicherheitszeit;
            if(dto.getSicherheitszeit() != 0) {
                sicherheitszeit = dto.getSicherheitszeit();
            } else {
                dto.setSicherheitszeit(1);
                sicherheitszeit = dto.getSicherheitszeit();
            }

            for (int i = 0; i < abstaendeStr.length; i++) {
                // 5. PRÜFUNG: Sind die Abstände gültige Zahlen? (NumberFormatException)
                try {
                    abstaende[i] = Integer.parseInt(abstaendeStr[i]);
                } catch (NumberFormatException e) {
                    Exceptionhandler.handle(e, "Formatfehler: Der Abstand '" + abstaendeStr[i] + "' ist keine gültige Ganzzahl.");
                }

                // 6. PRÜFUNG: Abstände dürfen nicht negativ sein
                if (abstaende[i] <= 0) {
                    Exceptionhandler.handle("Wertefehler: Ein Abstand darf nicht kleiner oder gleich 0 sein (Gefunden: " + abstaende[i] + ").");
                }

                // 7. PRÜFUNG: Abstand/Fahrzeit > (30 - Sicherheitszeit)
                if (abstaende[i] > (30 - sicherheitszeit)) {
                    Exceptionhandler.handle("Regelverletzung: Der Abstand (Fahrzeit) zwischen zwei Bahnhöfen ist zu groß: "
                            + abstaende[i] + " (Maximal erlaubt: " + (30 - sicherheitszeit) + ").");
                }

                summeAbstaende += abstaende[i];
            }
            dto.setAbstaende(abstaende);

            int haltezeiten = dto.getAnzahl() - 2;
            dto.setMinDauer(summeAbstaende + haltezeiten);

            scanner.nextLine(); // Leerzeile
            scanner.nextLine(); // "Start Hinfahrt:" überspringen

            // 8. PRÜFUNG: Ist die Startzeit eine gültige Zahl? (NumberFormatException)
            String startZeile = scanner.nextLine().trim();
            int start = 0;
            try {
                start = Integer.parseInt(startZeile);
            } catch (NumberFormatException e) {
                Exceptionhandler.handle(e, "Formatfehler: Die Startzeit '" + startZeile + "' ist keine gültige Ganzzahl.");
            }

            // 9. PRÜFUNG: Startzeit darf nicht negativ sein
            if (start < 0) {
                Exceptionhandler.handle("Wertefehler: Die Startzeit darf nicht negativ sein (Gefunden: " + start + ").");
            }

            dto.setStart(start);

            int umstiegszeit;
            if(dto.getUmstiegszeit() != 0) {
                umstiegszeit = dto.getUmstiegszeit();
            } else {
                dto.setUmstiegszeit(1);
                umstiegszeit = dto.getUmstiegszeit();
            }

            int anzahl = dto.getAnzahl();
            int[] hinweg = new int[anzahl * 2 - 2];
            int[] rueckweg = new int[anzahl * 2 - 2];
            String[] kollisionen = new String[anzahl - 1];
            int[] wartezeitHin = new int[anzahl];
            int[] wartezeitRueck = new int[anzahl];

            hinweg[0] = start;
            int temp = start;

            for (int i = 1; i < anzahl - 1; i++) {
                int num = (temp + dto.getAbstaende()[i - 1]) % 60;
                hinweg[2 * i - 1] = num;
                hinweg[2 * i] = (num + umstiegszeit) % 60;
                temp = hinweg[2 * i];
            }
            hinweg[hinweg.length - 1] =
                    (hinweg[hinweg.length - 2] + dto.getAbstaende()[dto.getAbstaende().length - 1]) % 60;

            int startRueck = (hinweg[hinweg.length - 1] + 1) % 60;
            rueckweg[rueckweg.length - 1] = startRueck;
            int tempRueck = startRueck;

            for (int i = anzahl - 2; i > 0; i--) {
                int ankunft = (tempRueck + dto.getAbstaende()[i]) % 60;
                rueckweg[2 * i] = ankunft;
                int abfahrt = (ankunft + umstiegszeit) % 60;
                rueckweg[2 * i - 1] = abfahrt;
                tempRueck = abfahrt;
            }
            rueckweg[0] = (tempRueck + dto.getAbstaende()[0]) % 60;

            Arrays.fill(kollisionen, "");

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
                for (int i = 0; i < dto.getWartezeitRueck().length; i++) {
                    gesamtWartezeitRueck += dto.getWartezeitRueck()[i];
                }
            }

            dto.setGesamtdauerHin(gesamtdauerHin + gesamtWartezeitHin);
            dto.setGesamtdauerRueck(gesamtdauerRueck + gesamtWartezeitRueck);

            dto.setHinweg(hinweg);
            dto.setRueckweg(rueckweg);
            dto.setKollisionen(kollisionen);
            dto.setWartezeitHin(wartezeitHin);
            dto.setWartezeitRueck(wartezeitRueck);
            dto.setStrafen(0);
            dto.setSicherheitszeit(1);

        } catch (FileNotFoundException e) {
            Exceptionhandler.handle(e, "Datei nicht gefunden: " + file.getPath());
        } catch (NoSuchElementException e) {
            Exceptionhandler.handle(e, "Formatfehler: Die Datei ist unvollständig oder hat nicht die erwartete Anzahl an Zeilen.");
        }

        return dto;
    }
}