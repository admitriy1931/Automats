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
        start.add(new LinearisedSymbol('a', 5));

        var end = new HashSet<LinearisedSymbol>();
        end.add(new LinearisedSymbol('c', 3));
        end.add(new LinearisedSymbol('c', 8));

        var pairs = new HashSet<Pair<LinearisedSymbol, LinearisedSymbol>>();

        pairs.add(makePair('a', 0, 'b', 1));
        pairs.add(makePair('a', 5, 'b', 6));
        pairs.add(makePair('b', 1, 'c', 3));
        pairs.add(makePair('b', 6, 'c', 8));

        var expected = new GlushkovSets(start, end, pairs);

        var actual = Assertions.assertDoesNotThrow(
                () -> GlushkovSetsBuild.makeGlushkovSets("abc + abc"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void wikiExampleTest(){
        var start = new HashSet<LinearisedSymbol>();
        start.add(new LinearisedSymbol('a', 0));
        start.add(new LinearisedSymbol('b', 7));

        var end = new HashSet<LinearisedSymbol>();
        end.add(new LinearisedSymbol('a', 0));
        end.add(new LinearisedSymbol('b', 2));
        end.add(new LinearisedSymbol('a', 8));

        var pairs = new HashSet<Pair<LinearisedSymbol, LinearisedSymbol>>();
        pairs.add(makePair('b', 2, 'a', 0));
        pairs.add(makePair('b', 2, 'a', 1));
        pairs.add(makePair('a', 1, 'b', 2));
        pairs.add(makePair('a', 0, 'a', 0));
        pairs.add(makePair('a', 8, 'b', 7));
        pairs.add(makePair('a', 0, 'a', 1));
        pairs.add(makePair('b', 7, 'a', 8));

        var expected = new GlushkovSets(start, end, pairs);

        var actual = Assertions.assertDoesNotThrow(
                () -> GlushkovSetsBuild.makeGlushkovSets("(a(ab)*)* + (ba)*"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void complexTest(){
        var start = new HashSet<LinearisedSymbol>();
        start.add(new LinearisedSymbol('a', 0));
        start.add(new LinearisedSymbol('b', 1));
        start.add(new LinearisedSymbol('d', 6));
        start.add(new LinearisedSymbol('e', 7));

        var end = new HashSet<LinearisedSymbol>();
        end.add(new LinearisedSymbol('a', 0));
        end.add(new LinearisedSymbol('c', 2));
        end.add(new LinearisedSymbol('b', 1));
        end.add(new LinearisedSymbol('f', 9));

        var pairs = new HashSet<Pair<LinearisedSymbol, LinearisedSymbol>>();
        pairs.add(makePair('f', 9, 'd', 6));
        pairs.add(makePair('f', 9, 'e', 7));
        pairs.add(makePair('b', 1, 'c', 2));
        pairs.add(makePair('c', 2, 'c', 2));
        pairs.add(makePair('d', 6, 'f', 9));
        pairs.add(makePair('e', 7, 'f', 9));

        var expected = new GlushkovSets(start, end, pairs);

        var actual = Assertions.assertDoesNotThrow(
                () -> GlushkovSetsBuild.makeGlushkovSets("a + bc* + ((d+e)f)*"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void complexProdTest(){
        var start = new HashSet<LinearisedSymbol>();
        start.add(new LinearisedSymbol('a', 0));
        start.add(new LinearisedSymbol('b', 1));

        var end = new HashSet<LinearisedSymbol>();
        end.add(new LinearisedSymbol('c', 3));
        end.add(new LinearisedSymbol('e', 5));

        var pairs = new HashSet<Pair<LinearisedSymbol, LinearisedSymbol>>();
        pairs.add(makePair('d', 4, 'e', 5));
        pairs.add(makePair('a', 0, 'c', 3));
        pairs.add(makePair('a', 0, 'd', 4));
        pairs.add(makePair('b', 1, 'c', 3));
        pairs.add(makePair('b', 1, 'd', 4));

        var expected = new GlushkovSets(start, end, pairs);

        var actual = Assertions.assertDoesNotThrow(
                () -> GlushkovSetsBuild.makeGlushkovSets("(a + b)(c + de)"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void concatenationTest(){
        var start = new HashSet<LinearisedSymbol>();
        start.add(new LinearisedSymbol('a', 0));

        var end = new HashSet<LinearisedSymbol>();
        end.add(new LinearisedSymbol('b', 3));

        var pairs = new HashSet<Pair<LinearisedSymbol, LinearisedSymbol>>();
        pairs.add(makePair('a', 1, 'b', 3));
        pairs.add(makePair('a', 0, 'a', 1));

        var expected = new GlushkovSets(start, end, pairs);

        var actual = Assertions.assertDoesNotThrow(
                () -> GlushkovSetsBuild.makeGlushkovSets("aab"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void concatenationWithBracketsTest(){
        var start = new HashSet<LinearisedSymbol>();
        start.add(new LinearisedSymbol('a', 0));

        var end = new HashSet<LinearisedSymbol>();
        end.add(new LinearisedSymbol('b', 2));

        var pairs = new HashSet<Pair<LinearisedSymbol, LinearisedSymbol>>();
        pairs.add(makePair('a', 1, 'b', 2));
        pairs.add(makePair('a', 0, 'a', 1));

        var expected = new GlushkovSets(start, end, pairs);

        var actual = Assertions.assertDoesNotThrow(
                () -> GlushkovSetsBuild.makeGlushkovSets("a(ab)"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void iterationTest(){
        var start = new HashSet<LinearisedSymbol>();
        start.add(new LinearisedSymbol('a', 0));

        var end = new HashSet<LinearisedSymbol>();
        end.add(new LinearisedSymbol('b', 1));

        var pairs = new HashSet<Pair<LinearisedSymbol, LinearisedSymbol>>();
        pairs.add(makePair('a', 0, 'b', 1));
        pairs.add(makePair('b', 1, 'a', 0));

        var expected = new GlushkovSets(start, end, pairs);

        var actual = Assertions.assertDoesNotThrow(
                () -> GlushkovSetsBuild.makeGlushkovSets("(ab)*"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void bigTest1(){
        var start = new HashSet<LinearisedSymbol>();
        start.add(new LinearisedSymbol('d', 4));
        start.add(new LinearisedSymbol('c', 0));
        start.add(new LinearisedSymbol('d', 1));

        var end = new HashSet<LinearisedSymbol>();
        end.add(new LinearisedSymbol('d', 6));
        end.add(new LinearisedSymbol('c', 0));

        var pairs = new HashSet<Pair<LinearisedSymbol, LinearisedSymbol>>();
        pairs.add(makePair('d', 2, 'd', 6));
        pairs.add(makePair('d', 1, 'd', 2));
        pairs.add(makePair('d', 4, 'd', 6));

        var expected = new GlushkovSets(start, end, pairs);

        var actual = Assertions.assertDoesNotThrow(
                () -> GlushkovSetsBuild.makeGlushkovSets("c + (dd+d)(d)"));
        Assertions.assertEquals(expected, actual);
    }

    private Pair<LinearisedSymbol, LinearisedSymbol> makePair(Character s1, Integer i1, Character s2, Integer i2){
        return new Pair<>(new LinearisedSymbol(s1, i1), new LinearisedSymbol(s2, i2));
    }
}
