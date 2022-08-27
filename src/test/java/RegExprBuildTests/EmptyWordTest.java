package RegExprBuildTests;

import algorithms.RegExprBuild;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import regexp.GrammarTree;
import regexp.RegexpExeption;

public class EmptyWordTest {
    public Boolean standardTest(String regexp){
        try {
            return RegExprBuild.allowEmptyWord(regexp);
        }
        catch (RegexpExeption regexpExeption){
            return null;
        }
    }

    @Test
    public void letterWithIterationPositiveTest(){
        var actual = standardTest("a*");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);
    }

    @Test
    public void sumWithIterationPositiveTest(){
        var actual = standardTest("a + b* + c");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);

        actual = standardTest("a + (b+c)* + c");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);
    }

    @Test
    public void concatWithIterationPositiveTest(){
        var actual = standardTest("a*b*c*");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);

        actual = standardTest("a*(bc)*c*");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);
    }

    @Test
    public void concatWithIterationNegativeTest(){
        var actual = standardTest("a*bc*");

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual);

        actual = standardTest("a*(bc)c*");

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual);
    }

    @Test
    public void letterWithIterationNegativeTest(){
        var actual = standardTest("a");

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual);
    }

    @Test
    public void sumWithIterationNegativeTest(){
        var actual = standardTest("a + b + c");

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual);

        actual = standardTest("a + (b+c) + c");

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual);
    }

    @Test
    public void complexPositiveTest(){
        var actual = standardTest("a + bb + (c+c)(c+c) + (d*d* + dd)(dd + d*d*)");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);
    }

    @Test
    public void bigNegativeTest1(){
        var actual = standardTest("c + (dd+d)(d)");

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual);
    }

    @Test
    public void emptyWordSymbolSumTest(){
        var actual = standardTest("a + -");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);
    }

    @Test
    public void emptyWordSymbolSumMultiplyTest(){
        var actual = standardTest("a + bc + -");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);
    }

    @Test
    public void emptyWordSymbolMultiplySumTest(){
        var actual = standardTest("a(bc + -)");

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual);
    }

    @Test
    public void emptyWordSymbolIterationSumTest(){
        var actual = standardTest("a*(c + -)");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);
    }

    @Test
    public void emptyWordSymbolSumSumConcatTest(){
        var actual = standardTest("(a+-)(b + -)");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);
    }

    @Test
    public void emptyWordSymbolExamTest(){
        var actual = standardTest("b*(ab*ab*a + -)");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);
    }

    @Test
    public void emptyWordSymbolExam1Test(){
        var actual = standardTest("b(ba)*(b(b + -) + a) + -");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);
    }

    @Test
    public void emptyWordSymbolExam2Test(){
        var actual = standardTest("a(aa + -) + b + -");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);
    }

    @Test
    public void emptyWordSymbolExam3Test(){
        var actual = standardTest("a(aa + -) + b + -");

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);
    }

}
