SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" &> /dev/null && pwd)"

cd "$SCRIPT_DIR" || exit 1

JAR_DIR="jar"
INPUT_DIR="input"

JAR_FILE=$(ls "$JAR_DIR"/*.jar 2>/dev/null | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "Fehler: Es wurde keine .jar Datei im Ordner '$JAR_DIR' gefunden!"
    exit 1
fi

echo "Verwende JAR-Datei: $(basename "$JAR_FILE")"
echo "------------------------------------------------"

if [ ! -d "$INPUT_DIR" ] || [ -z "$(ls -A "$INPUT_DIR")" ]; then
    echo "Fehler: Der Ordner 'input' existiert nicht oder ist leer!"
    exit 1
fi

for FILE in "$INPUT_DIR"/*; do
    if [ ! -f "$FILE" ]; then
        continue
    fi

    FILENAME=$(basename "$FILE")

    echo "Verarbeite Datei: $FILENAME"

    JAVA_INPUT_PFAD="input/$FILENAME"

    java -jar "$JAR_FILE" "$JAVA_INPUT_PFAD"

    echo "Datei wurde verarbeitet."
    echo "------------------------------------------------"
done

echo "Alle Dateien wurden verarbeitet!"