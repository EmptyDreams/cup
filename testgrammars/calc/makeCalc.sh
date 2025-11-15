rm -rf ./java
mkdir java

cp scanner.java java/scanner.java

java -jar ../../build/libs/java-cup-11b.jar -interface -parser CalcParser -symbols CalcSymbols -destdir ./java calc.cup
javac -cp ../../build/libs/java-cup-11b-runtime.jar -d ./java/classes ./java/*.java

java -cp "../../build/libs/java-cup-11b-runtime.jar;./java/classes/" Main