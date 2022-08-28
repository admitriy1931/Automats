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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlushkovSets sets = (GlushkovSets) o;
        return Objects.equals(setOfStartSymbol, sets.setOfStartSymbol)
                && Objects.equals(setOfEndSymbol, sets.setOfEndSymbol)
                && Objects.equals(setOfPairs, sets.setOfPairs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(setOfStartSymbol, setOfEndSymbol, setOfPairs);
    }
}
