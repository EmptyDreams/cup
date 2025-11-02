mkdir -p ./java/classes
rm ./java/classes/*.class
java -jar ../../bin/JFlex.jar minijava.jflex
java -jar ../../target/dist/java-cup-11b.jar \
  -interface \
  -parser MiniJavaParser \
  -symbols MiniJavaSymbols \
  -nonterms \
  -xmlactions \
  -destdir ./java \
  -ast Node%s \
  minijava.cup
java -cp ../../target/dist/java-cup-11b-runtime.jar:. ./java/classes/MiniJavaParser simple.minijava simple.xml /
basex codegen.sq output.xml > simple.minijvm