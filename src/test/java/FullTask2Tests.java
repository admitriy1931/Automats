import algorithms.GlushkovAlgo;
import algorithms.Isomorphism;
import automat.Automat;
import automat.IsomorphismResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import regexp.RegexpExeption;

public class FullTask2Tests {
    public IsomorphismResult automatsAreIsomorphic(Automat aut1, Automat aut2){
        try {
            return Isomorphism.automatsAreIsomorphic(aut1, aut2);
        }
        catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return null;
    }

    private Automat doGlushkovAlgo(String regexp){
        try
        {
            return GlushkovAlgo.doGlushkovAlgo(regexp);
        }
        catch (RegexpExeption e){
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void plusIterationTest(){
        var aut1 = doGlushkovAlgo("(b + c)*");
        var aut2 = doGlushkovAlgo("b + c");
        var expected = new IsomorphismResult("", null ,null);

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void bigRegexp1and2Test(){
        var aut1 = doGlushkovAlgo("c + (dd+d)(d)");
        var aut2 = doGlushkovAlgo("(d+d)(e+e)+(f+f)");

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(null, actual);
    }

    @Test
    public void bigRegexp3and4Test(){
        var aut1 = doGlushkovAlgo("(c+((d+d)(e+e)+(f+f))(d))");
        var aut2 = doGlushkovAlgo("(c+((d+d)(e+e)+(f+f))(d)) + g");

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(null, actual);
    }

    @Test
    public void bigRegexp4and5Test(){
        var aut1 = doGlushkovAlgo("(c+((d+d)(e+e)+(f+f))(d)) + g");
        var aut2 = doGlushkovAlgo("((c+((d+d)(e+e)+(f+f))(d)) + g) + h");

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(null, actual);
    }

    @Test
    public void complexProdTest(){
        var aut1 = doGlushkovAlgo("(a + b)(c + de)");
        var aut2 = doGlushkovAlgo("(a + b)de");

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(null, actual);
    }

    @Test
    public void plusWithBracketsAndIterationTest(){
        var aut1 = doGlushkovAlgo("a + (b + c)* + d");
        var aut2 = doGlushkovAlgo("a + b + c + d");

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(null, actual);
    }

    @Test
    public void plusMultiplyIterationTest(){
        var aut1 = doGlushkovAlgo("(a + b + cd)*");
        var aut2 = doGlushkovAlgo("(a + b + c)*");

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(null, actual);
    }

    @Test
    public void wikiExampleTest(){
        var aut1 = doGlushkovAlgo("(a(ab)*)* + (ba)*");
        var aut2 = doGlushkovAlgo("(a(ab)*)* + (b)*");

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(null, actual);
    }

    @Test
    public void sumSumSumTest(){
        var aut1 = doGlushkovAlgo("ab + (c+d)");
        var aut2 = doGlushkovAlgo("ab + (c+d+e)");

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(null, actual);
    }

    @Test
    public void letterSumSumTest(){
        var aut1 = doGlushkovAlgo("a + (b+c)");
        var aut2 = doGlushkovAlgo("a + (b+cd)");

        var actual = automatsAreIsomorphic(aut1, aut2);

        Assertions.assertEquals(null, actual);
    }
}
