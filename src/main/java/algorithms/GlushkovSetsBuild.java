package algorithms;

import javafx.util.Pair;
import regexp.GlushkovSets;
import regexp.GrammarTree;
import regexp.LinearisedSymbol;
import regexp.RegexpExeption;

import java.util.HashSet;
import java.util.Objects;

public class GlushkovSetsBuild {
    public static GlushkovSets makeGlushkovSets(String regexp) throws RegexpExeption {
        var tree = RegExprBuild.makeGrammarTree(regexp);

        return new GlushkovSets(
                makeSetOfStartSymbols(tree),
                makeSetOfEndSymbols(tree),
                makeSetOfPairs(tree));
    }

    private static HashSet<LinearisedSymbol> makeSetOfStartSymbols(GrammarTree tree){
        HashSet<LinearisedSymbol> symbols = new HashSet<>();
        if (Objects.equals(tree.value, "+")){
            for (GrammarTree child: tree.children){
                symbols.addAll(makeSetOfStartSymbols(child));
                if (child.canBeEmpty)
                    tree.canBeEmpty = true;
            }
        }
        else if (Objects.equals(tree.value, RegExprBuild.CON)){
            tree.canBeEmpty = true;
            for (GrammarTree child: tree.children) {
                symbols.addAll(makeSetOfStartSymbols(child));
                if (!child.iterationAvailable && !child.canBeEmpty){
                    tree.canBeEmpty = false;
                    break;
                }
            }
        }
        else if (tree.value.length() == 1) {
            if (!tree.value.equals(RegExprBuild.emptyWordSymbol))
                symbols.add(new LinearisedSymbol(tree.value.charAt(0), tree.position));
            else
                tree.canBeEmpty = true;
        }

        return symbols;
    }

    private static HashSet<LinearisedSymbol> makeSetOfEndSymbols(GrammarTree tree){
        var symbols = new HashSet<LinearisedSymbol>();
        if (Objects.equals(tree.value, "+")){
            for (GrammarTree child: tree.children){
                symbols.addAll(makeSetOfEndSymbols(child));
                if (child.canBeEmpty)
                    tree.canBeEmpty = true;
            }
        }
        else if (Objects.equals(tree.value, RegExprBuild.CON)){
            tree.canBeEmpty = true;
            for (var i = tree.children.size()-1; i >= 0; i--){
                var child = tree.children.get(i);
                symbols.addAll(makeSetOfEndSymbols(child));
                if (!child.iterationAvailable && !child.canBeEmpty){
                    tree.canBeEmpty = false;
                    break;
                }
            }
        }
        else if (tree.value.length() == 1) {
            if (!tree.value.equals(RegExprBuild.emptyWordSymbol))
                symbols.add(new LinearisedSymbol(tree.value.charAt(0), tree.position));
            else
                tree.canBeEmpty = true;
        }

        return symbols;
    }

    private static HashSet<Pair<LinearisedSymbol, LinearisedSymbol>> makePairs(
            HashSet<LinearisedSymbol> first, HashSet<LinearisedSymbol> second){
        var pairs = new HashSet<Pair<LinearisedSymbol, LinearisedSymbol>>();
        for (LinearisedSymbol x : first)
            for (LinearisedSymbol y : second)
                pairs.add(new Pair<>(x, y));

        return pairs;
    }

    private static HashSet<Pair<LinearisedSymbol, LinearisedSymbol>> makeSetOfPairs(GrammarTree tree){
        var pairs = new HashSet<Pair<LinearisedSymbol, LinearisedSymbol>>();
        if (Objects.equals(tree.value, "+")){
            for (GrammarTree child: tree.children)
                pairs.addAll(makeSetOfPairs(child));
        }
        else if (Objects.equals(tree.value, RegExprBuild.CON)){
            for (var i = 0; i < tree.children.size(); i++){
                var child = tree.children.get(i);
                var ourTail = makeSetOfEndSymbols(child);
                for (var j = i+1; j < tree.children.size(); j++){
                    var neighbourChild = tree.children.get(j);
                    pairs.addAll(makePairs(ourTail, makeSetOfStartSymbols(neighbourChild)));
                    if (!neighbourChild.iterationAvailable && !neighbourChild.canBeEmpty)
                        break;
                }
                if (child.iterationAvailable)
                    pairs.addAll(makePairs(ourTail, makeSetOfStartSymbols(child)));
                pairs.addAll(makeSetOfPairs(child));
            }
            if (tree.iterationAvailable)
                pairs.addAll(makePairs(makeSetOfEndSymbols(tree), makeSetOfStartSymbols(tree)));
        }
        else if (tree.value.length() == 1) {
            if (tree.iterationAvailable){
                var symbol = new LinearisedSymbol(tree.value.charAt(0), tree.position);
                pairs.add(new Pair<>(symbol, symbol));
            }
        }

        return pairs;
    }
}
