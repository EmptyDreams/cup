# CUP Parser Generator (Community Fork)

An independent, community-maintained fork of the original **CUP (Construction of Useful Parsers)** parser generator for Java.

## 🚀 Key Improvements
- **Modern Gradle build system** (replaces legacy Ant)
- **Automatic AST generation** via the `-ast` flag (e.g., `Node%s`)
- **Extended BNF syntax** and cleaner grammar support
- **Reduced legacy code**: Removed XML output and other unused features
- **Integrated tests**: Includes MiniJava and calculator examples with automated validation

## 🛠️ Build & Use

Prerequisites: JDK 11+, Gradle (or use wrapper)

```bash
# Generate parser and lexer sources
./gradlew generateCupParser generateJFlexLexer

# Build main and runtime JARs
./gradlew jar

# Output:
# - build/libs/java-cup-11b.jar          (main tool)
# - build/libs/java-cup-11b-runtime.jar  (runtime library for generated parsers)
