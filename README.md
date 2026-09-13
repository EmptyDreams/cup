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
# Build main and runtime JARs — the bootstrap sources (CupParser,
# GrammarSymConstants, Lexer and the -ast node classes) are committed under
# src/generated, so a fresh clone needs no prerequisite generation step
./gradlew jar

# Regenerate CUP's own parser/lexer from the grammar (runs jflex too) and
# verify the output still matches the committed sources byte-for-byte
./gradlew generateCupParser

# Stronger self-hosting gate: regenerate with the jar built from the working
# tree and byte-compare against the committed sources -- fails when the
# generator itself regressed, not just when the grammar changed
./gradlew checkSelfHost
```

## 📦 Release

Configure the JReleaser credentials and signing keys through environment
variables or `~/.jreleaser/config.toml`, then run:

```bash
# Validate the JReleaser configuration
./gradlew jreleaserConfig

# Remove previous build outputs
./gradlew clean

# Build and publish artifacts to the local staging repository
./gradlew publish

# Sign and deploy the staged artifacts to Maven Central
./gradlew jreleaserDeploy
```

On Windows, use `gradlew.bat` instead of `./gradlew`.