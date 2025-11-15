#!/bin/bash

mkdir -p ./java/classes
rm ./java/classes/*.class

java -jar ../../bin/JFlex.jar c.jflex -d ./java
java -jar ../../build/libs/java-cup-11b.jar \
  -interface \
  -parser CParser \
  -symbols CSymbols \
  -nonterms \
  -destdir ./java \
  -ast Node%s \
  c.cup

javac -cp "../../build/libs/java-cup-11b.jar" -d ./java/classes ./java/*.java

ACTUAL=$(java -cp "../../build/libs/java-cup-11b-runtime.jar;./java/classes/" CParser complicated.c)
EXPECTED=$(cat simple.output)

normalize() {
    local lines=()
    while IFS= read -r line; do
        line="${line%$'\r'}"
        line="${line%"${line##*[![:space:]]}"}"
        lines+=("$line")
    done <<< "$1"

    local last_non_empty=-1
    for (( i=${#lines[@]}-1; i>=0; i-- )); do
        if [[ -n "${lines[i]}" ]]; then
            last_non_empty=$i
            break
        fi
    done

    for (( i=0; i<=last_non_empty; i++ )); do
        printf '%s\n' "${lines[i]}"
    done
}

ACTUAL=$(normalize "$ACTUAL_RAW")
EXPECTED=$(normalize "$EXPECTED_RAW")

if [ "$ACTUAL" = "$EXPECTED" ]; then
    echo "PASS"
else
    echo "FAIL"
    echo "Diff:"
    diff -u <(echo "$EXPECTED") <(echo "$ACTUAL")
fi

rm -rf ./java