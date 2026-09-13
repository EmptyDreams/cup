package java_cup.spec;

import java.util.List;

/**
 * Root of the sugar-preserving specification tree produced by the meta
 * parser ({@code spec} in parser.cup). One node per .cup file; every list
 * keeps strict source order, which the Lowering pass relies on when it
 * reproduces the historical creation order of terminals, non-terminals and
 * productions.
 *
 * <p>The tree models what the user wrote: quantifiers ({@code X?},
 * {@code X*}, {@code [a,b]*?}), spreads ({@code ...x}), labels,
 * {@code ::T} annotations and anonymous expressions ({@code (A|B)}) all
 * survive until lowering -- nothing is desugared here.</p>
 */
public final class SpecNode {

    /** Declared package, or {@code null} when the spec has no {@code package} directive. */
    public final String packageName;

    /** {@code import} directives in source order. */
    public final List<ImportNode> imports;

    /** {@code class} directive name, or {@code null} when absent (emit keeps its default). */
    public final String className;

    /** Action/parser/init/scan code parts in source order. */
    public final List<CodePartNode> codeParts;

    /** Terminal and non-terminal declarations in source order. */
    public final List<SymbolDeclNode> symbolDecls;

    /** {@code precedence} declarations in source order. */
    public final List<PrecedenceNode> precedences;

    /** Name from {@code start with}, or {@code null} when absent. */
    public final String startName;

    /** Productions in source order. */
    public final List<ProductionNode> productions;

    /**
     * Set when the file was salvaged through the {@code spec ::= error ...}
     * recovery production. Sections parsed before the error are lost from
     * this tree (accepted divergence: code generation is abandoned when any
     * parse error occurred).
     */
    public final boolean hasParseError;

    public SpecNode(
            String packageName,
            List<ImportNode> imports,
            String className,
            List<CodePartNode> codeParts,
            List<SymbolDeclNode> symbolDecls,
            List<PrecedenceNode> precedences,
            String startName,
            List<ProductionNode> productions,
            boolean hasParseError) {
        this.packageName = packageName;
        this.imports = imports;
        this.className = className;
        this.codeParts = codeParts;
        this.symbolDecls = symbolDecls;
        this.precedences = precedences;
        this.startName = startName;
        this.productions = productions;
        this.hasParseError = hasParseError;
    }
}
