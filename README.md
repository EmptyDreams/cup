# CUP Parser Generator (Community Fork)

An independent, community-maintained fork of the original **CUP (Construction of Useful Parsers)** parser generator for Java.

## 🚀 Differences from the Original java-cup

This fork keeps the core CUP parser-generation workflow while modernizing the
build and extending grammar support:

- Uses Gradle instead of the original Ant-based build.
- Adds automatic AST generation through the `-ast` flag, for example `Node%s`.
- Provides extended BNF syntax and cleaner grammar support.
- Removes XML output and other unused legacy features.
- Includes automated MiniJava and calculator examples.

## 🛠️ Build & Use

Prerequisites: JDK 11+, Gradle (or use the wrapper)

```bash
# Generate parser and lexer sources
./gradlew generateCupParser generateJFlexLexer

# Build main and runtime JARs
./gradlew jar
```

## 📦 Release

Configure the JReleaser credentials and signing keys through environment
variables or `~/.jreleaser/config.toml`, then run:

```bash
# Validate the JReleaser configuration
./gradlew jreleaserConfig

# Remove previous build outputs
./gradlew clean

# Generate CUP parser and JFlex lexer sources
./gradlew generateCupParser generateJFlexLexer

# Build and publish artifacts to the local staging repository
./gradlew publish

# Sign and deploy the staged artifacts to Maven Central
./gradlew jreleaserDeploy
```

On Windows, use `gradlew.bat` instead of `./gradlew`.