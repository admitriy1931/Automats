package RegExprBuildTests;

import algorithms.GlushkovSetsBuild;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import regexp.GlushkovSets;
import regexp.LinearisedSymbol;

import java.util.HashSet;

public class GlushkovSetsBuildTest {
    @Test
    public void simpleTest(){
        var start = new HashSet<LinearisedSymbol>();
        start.add(new LinearisedSymbol('a', 0));
        start.add(new LinearisedSymbol('a', 6));

        var end = new HashSet<LinearisedSymbol>();
        end.add(new LinearisedSymbol('c', 2));
        end.add(new LinearisedSymbol('c', 8));

        var pairs = new HashSet<Pair<LinearisedSymbol, LinearisedSymbol>>();

        pairs.add(makePair('a', 0, 'b', 1));
        pairs.add(makePair('b', 1, 'c', 2));
        pairs.add(makePair('a', 6, 'b', 7));
        pairs.add(makePair('b', 7, 'c', 8));

        var expected = new GlushkovSets(start, end, pairs);

        var actual = Assertions.assertDoesNotThrow(
                () -> GlushkovSetsBuild.makeGlushkovSets("abc + abc"));

        Assertions.assertTrue(expected.glushkovSetsAreEqual(actual));
    }

    @Test
    public void wikiExampleTest(){
        var start = new HashSet<LinearisedSymbol>();
        start.add(new LinearisedSymbol('a', 1));
        start.add(new LinearisedSymbol('b', 13));

        var end = new HashSet<LinearisedSymbol>();
        end.add(new LinearisedSymbol('a', 1));
        end.add(new LinearisedSymbol('b', 4));
        end.add(new LinearisedSymbol('a', 14));

        var pairs = new HashSet<Pair<LinearisedSymbol, LinearisedSymbol>>();
        pairs.add(makePair('a', 1, 'a', 3));
        pairs.add(makePair('a', 1, 'a', 1));
        pairs.add(makePair('a', 3, 'b', 4));
        pairs.add(makePair('b', 4, 'a', 1));
        pairs.add(makePair('b', 4, 'a', 3));
        pairs.add(makePair('b', 13, 'a', 14));
        pairs.add(makePair('a', 14, 'b', 13));

        var expected = new GlushkovSets(start, end, pairs);

        var actual = Assertions.assertDoesNotThrow(
                () -> GlushkovSetsBuild.makeGlushkovSets("(a(ab)*)* + (ba)*"));

        Assertions.assertTrue(expected.glushkovSetsAreEqual(actual));
    }

    @Test
    public void complexTest(){
        var start = new HashSet<LinearisedSymbol>();
        start.add(new LinearisedSymbol('a', 0));
        start.add(new LinearisedSymbol('b', 4));
        start.add(new LinearisedSymbol('d', 12));
        start.add(new LinearisedSymbol('e', 14));

        var end = new HashSet<LinearisedSymbol>();
        end.add(new LinearisedSymbol('a', 0));
        end.add(new LinearisedSymbol('b', 4));
        end.add(new LinearisedSymbol('c', 5));
        end.add(new LinearisedSymbol('f', 16));

        var pairs = new HashSet<Pair<LinearisedSymbol, LinearisedSymbol>>();
        pairs.add(makePair('b', 4, 'c', 5));
        pairs.add(makePair('c', 5, 'c', 5));
        pairs.add(makePair('d', 12, 'f', 16));
        pairs.add(makePair('e', 14, 'f', 16));
        pairs.add(makePair('f', 16, 'd', 12));
        pairs.add(makePair('f', 16, 'e', 14));

        var expected = new GlushkovSets(start, end, pairs);

        var actual = Assertions.assertDoesNotThrow(
                () -> GlushkovSetsBuild.makeGlushkovSets("a + bc* + ((d+e)f)*"));

        Assertions.assertTrue(expected.glushkovSetsAreEqual(actual));
    }

    @Test
    public void complexProdTest(){
        var start = new HashSet<LinearisedSymbol>();
        start.add(new LinearisedSymbol('a', 1));
        start.add(new LinearisedSymbol('b', 5));

        var end = new HashSet<LinearisedSymbol>();
        end.add(new LinearisedSymbol('c', 8));
        end.add(new LinearisedSymbol('e', 13));

        var pairs = new HashSet<Pair<LinearisedSymbol, LinearisedSymbol>>();
        pairs.add(makePair('a', 1, 'c', 8));
        pairs.add(makePair('a', 1, 'd', 12));
        pairs.add(makePair('b', 5, 'c', 8));
        pairs.add(makePair('b', 5, 'd', 12));
        pairs.add(makePair('d', 12, 'e', 13));

        var expected = new GlushkovSets(start, end, pairs);

        var actual = Assertions.assertDoesNotThrow(
                () -> GlushkovSetsBuild.makeGlushkovSets("(a + b)(c + de)"));

        Assertions.assertTrue(expected.glushkovSetsAreEqual(actual));
    }

    @Test
    public void concatenationTest(){
        var start = new HashSet<LinearisedSymbol>();
        start.add(new LinearisedSymbol('a', 0));

        var end = new HashSet<LinearisedSymbol>();
        end.add(new LinearisedSymbol('b', 2));

        var pairs = new HashSet<Pair<LinearisedSymbol, LinearisedSymbol>>();
        pairs.add(makePair('a', 0, 'a', 1));
        pairs.add(makePair('a', 1, 'b', 2));

        var expected = new GlushkovSets(start, end, pairs);

        var actual = Assertions.assertDoesNotThrow(
                () -> GlushkovSetsBuild.makeGlushkovSets("aab"));

        Assertions.assertTrue(expected.glushkovSetsAreEqual(actual));
    }

    @Test
    public void concatenationWithBracketsTest(){
        var start = new HashSet<LinearisedSymbol>();
        start.add(new LinearisedSymbol('a', 0));

        var end = new HashSet<LinearisedSymbol>();
        end.add(new LinearisedSymbol('b', 3));

        var pairs = new HashSet<Pair<LinearisedSymbol, LinearisedSymbol>>();
        pairs.add(makePair('a', 0, 'a', 2));
        pairs.add(makePair('a', 2, 'b', 3));

        var expected = new GlushkovSets(start, end, pairs);

        var actual = Assertions.assertDoesNotThrow(
                () -> GlushkovSetsBuild.makeGlushkovSets("a(ab)"));

        Assertions.assertTrue(expected.glushkovSetsAreEqual(actual));
    }

    @Test
    public void iterationTest(){
        var start = new HashSet<LinearisedSymbol>();
        start.add(new LinearisedSymbol('a', 1));

        var end = new HashSet<LinearisedSymbol>();
        end.add(new LinearisedSymbol('b', 2));

        var pairs = new HashSet<Pair<LinearisedSymbol, LinearisedSymbol>>();
        pairs.add(makePair('a', 1, 'b', 2));
        pairs.add(makePair('b', 2, 'a', 1));

        var expected = new GlushkovSets(start, end, pairs);

        var actual = Assertions.assertDoesNotThrow(
                () -> GlushkovSetsBuild.makeGlushkovSets("(ab)*"));

        Assertions.assertTrue(expected.glushkovSetsAreEqual(actual));
    }



    private Pair<LinearisedSymbol, LinearisedSymbol> makePair(Character s1, Integer i1, Character s2, Integer i2){
        return new Pair<>(new LinearisedSymbol(s1, i1), new LinearisedSymbol(s2, i2));
    }
}
