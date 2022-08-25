package regexp;

import javafx.util.Pair;

import java.util.*;

public class GlushkovSets {
    private final HashSet<LinearisedSymbol> setOfStartSymbol;
    private final HashSet<LinearisedSymbol> setOfEndSymbol;
    private final HashSet<Pair<LinearisedSymbol, LinearisedSymbol>> setOfPairs;

    public GlushkovSets(HashSet<LinearisedSymbol> setOfStartSymbol,
                        HashSet<LinearisedSymbol> setOfEndSymbol,
                        HashSet<Pair<LinearisedSymbol, LinearisedSymbol>> setOfPairs){
        this.setOfStartSymbol = setOfStartSymbol;
        this.setOfEndSymbol = setOfEndSymbol;
        this.setOfPairs = setOfPairs;
    }

    public HashSet<LinearisedSymbol> getSetOfStartSymbol() {
        return setOfStartSymbol;
    }

    public HashSet<LinearisedSymbol> getSetOfEndSymbol() {
        return setOfEndSymbol;
    }

    public HashSet<Pair<LinearisedSymbol, LinearisedSymbol>> getSetOfPairs() {
        return setOfPairs;
    }

    public Boolean glushkovSetsAreEqual(GlushkovSets other){
        return setOfPairs.equals(other.getSetOfPairs()) &&
                !setOfEndSymbol.equals(other.getSetOfEndSymbol()) &&
                (!setOfStartSymbol.equals(other.getSetOfStartSymbol()));
    }
}
