package java_cup.ast;

import java_cup.*;
import java_cup.runtime.ArrayStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AstNodeBuilder {

    private AstNodeBuilder() {}

    private static final Map<non_terminal, VirtualType> typeCache = new HashMap<>();

    public static VirtualType buildGraph(GrammarSymbol sym) throws internal_error {
        if (sym.is_non_term() && ("AstNode".equals(sym.stack_type()))) {
            return buildGraph((non_terminal) sym, null, -1);
        } else {
            return VirtualType.ofBasic(sym.stack_type());
        }
    }

    private static VirtualType buildGraph(non_terminal nt, Production fromProd, int index) throws internal_error {
        if (nt.isAnno() && fromProd == null) return null;
        if (typeCache.containsKey(nt)) return typeCache.get(nt);
        var type = new VirtualType(true, "");
        if (type.isAnno) {
            type.className = emit.getAnnoExprName((non_terminal) fromProd.lhs().the_symbol(), fromProd, index);
        } else {
            type.className = nt.astClassName();
        }
        if (!nt.isAnno()) typeCache.put(nt, type);
        Map<String, VirtualProduction> prods = new HashMap<>();
        if (nt.isOptBox()) {
            var subSymbol = nt.getOptContent();
            VirtualType result;
            if (subSymbol.is_non_term()) {
                result = buildGraph((non_terminal) subSymbol, fromProd, index);
            } else {
                result = buildGraph(subSymbol);
            }
            return result;
        } else if (nt.isListBox()) {
            var subSymbol = nt.getListElementContent();
            VirtualType result;
            if (subSymbol.is_non_term()) {
                result = buildGraph((non_terminal) subSymbol, fromProd, index);
            } else {
                result = buildGraph(subSymbol);
            }
            if (result == null) return null;
            result.castToBox();
            return result.toList();
        }
        var annoItor = nt.isAnno() ? non_terminal.getAnnoLabelAndAction(fromProd).iterator() : null;
        for (Production prod : nt.productions()) {
            if (prod.hasTailAction()) continue;
            var name = prod.getProdName();
            if (prods.containsKey(name)) {
                prods.get(name).srcExprs.add(prod.to_simple_string());
                continue;
            }
            // If the current symbol is an anonymous symbol, read label and action through annoItor
            var annoInfo = annoItor == null ? null : annoItor.next();
            var annoLabelList = annoInfo == null ? null : annoInfo.getLabelList();
            if (annoInfo != null && annoInfo.getAction() != null) continue;
            List<VirtualField> fields = new ArrayStack<>();
            int annoCount = 0;
            for (int i = 0; i < prod.rhs_length(); i++) {
                var rhs = prod.rhs(i);
                if (rhs.is_action()) continue;
                var symbolPart = (symbol_part) rhs;
                var symbol = symbolPart.the_symbol();
                String label = annoLabelList == null ? symbolPart.label() : annoLabelList.get(i);
                // Skip symbols without label and not inline
                if (label == null && !symbolPart.isInline()) continue;
                var subNt = symbol.is_non_term() ? (non_terminal) symbol : null;
                if (subNt != null && subNt.isAnno()) {
                    var annoIndex = annoCount++;
                    var subType = buildGraph(subNt, prod, annoIndex);
                    if (subType == null) continue;
                    fields.add(new VirtualField(label, subType, symbolPart));
                } else {
                    var subType = buildGraph(symbol);
                    fields.add(new VirtualField(label, subType, symbolPart));
                }
            }
            var newProd = new VirtualProduction(name, fields);
            newProd.srcExprs.add(prod.to_simple_string());
            prods.put(name, newProd);
        }
        if (!nt.isLaAnno() && nt.isAnno()) return null;
        type.prods = List.copyOf(prods.values());
        type.isAnno = fromProd != null;
        return type;
    }

}